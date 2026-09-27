package utilities;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;

/**
 * GeminiClient: gọi Gemini 2.5 Pro (v1beta) bằng OkHttp + parse an toàn + retry 429/503.
 * Phụ thuộc: okhttp3 (>=4.11), org.json (20230227+)
 */
public class GeminiClient {
    // ⚠️ Demo: hardcode theo yêu cầu của bạn. Dự án thật: dùng biến môi trường.
    private static final String API_KEY = "AIzaSyBTLOUFyBgwajIzlj0XwvxARh4RlPNO5Zc";
    private static final String MODEL   = "gemini-2.5-flash";

    private static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    // OkHttpClient với timeout & retry kết nối (không phải retry HTTP)
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .callTimeout(Duration.ofSeconds(90))
            .connectTimeout(Duration.ofSeconds(30))
            .readTimeout(Duration.ofSeconds(60))
            .writeTimeout(Duration.ofSeconds(60))
            .retryOnConnectionFailure(true)
            // Nếu nghi ngờ HTTP/2 lỗi mạng, bỏ comment để ép HTTP/1.1:
            // .protocols(java.util.Arrays.asList(Protocol.HTTP_1_1))
            .build();

    public static String callGemini(String prompt) throws Exception {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("Missing API key");
        }

        final String url =
                "https://generativelanguage.googleapis.com/v1beta/models/"
                        + MODEL + ":generateContent?key=" + API_KEY;

        // Build body JSON (tránh nối chuỗi thủ công)
        JSONObject part = new JSONObject().put("text", prompt);
        JSONObject content = new JSONObject().put("parts", new JSONArray().put(part));
        JSONObject genCfg = new JSONObject()
                .put("temperature", 0.7)
                .put("maxOutputTokens", 1024);
        // Không ép response_mime_type để tránh format lạ khi tool-calling

        JSONObject body = new JSONObject()
                .put("contents", new JSONArray().put(content))
                .put("generationConfig", genCfg);

        RequestBody reqBody = RequestBody.create(body.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(reqBody)
                .header("Accept", "application/json")
                .build();

        int attempts = 0;
        int maxAttempts = 7; // tăng thêm để vượt 503 tạm thời
        while (true) {
            attempts++;
            try (Response response = CLIENT.newCall(request).execute()) {
                String raw = response.body() != null ? response.body().string() : "";
                if (response.isSuccessful()) {
                    JSONObject root = new JSONObject(raw);
                    String text = extractText(root);
                    if (text == null || text.isEmpty()) {
                        // Log raw để debug nếu cần
                        throw new RuntimeException("Empty text in success response: " + raw);
                    }
                    return text;
                } else {
                    int code = response.code();
                    // Retry cho 503/429 (tạm thời quá tải/giới hạn)
                    if ((code == 503 || code == 429) && attempts < maxAttempts) {
                        // Exponential backoff + jitter (trần 12s/lần)
                        long base = 700L * (long) Math.pow(2, (attempts - 1)); // 0.7s,1.4s,2.8s,...
                        long jitter = (long) (Math.random() * 600);
                        Thread.sleep(Math.min(12_000L, base + jitter));
                        continue;
                    }
                    throw new RuntimeException("HTTP " + code + " - " + raw);
                }
            } catch (java.net.SocketTimeoutException e) {
                if (attempts < maxAttempts) {
                    long backoff = Math.min(12_000L, 700L * (long) Math.pow(2, (attempts - 1)));
                    Thread.sleep(backoff);
                    continue;
                }
                throw e;
            }
        }
    }

    /** Parse “đa đường” để lấy text, tránh vỡ khi parts thay đổi cấu trúc */
    private static String extractText(JSONObject root) {
        // 1) Lỗi chuẩn top-level
        if (root.has("error")) {
            JSONObject err = root.getJSONObject("error");
            String msg = err.optString("message", "Unknown error");
            int code = err.optInt("code", -1);
            throw new RuntimeException("Gemini error " + code + " - " + msg);
        }

        // 2) promptFeedback báo chặn safety
        if (root.has("promptFeedback")) {
            JSONObject pf = root.getJSONObject("promptFeedback");
            // Một số response đặt blockReason là string; dùng opt thay vì get
            String blockReason = pf.optString("blockReason", "");
            if (!blockReason.isEmpty()) {
                return "[Blocked by safety — " + blockReason + "]";
            }
        }

        // 3) candidates
        JSONArray candidates = root.optJSONArray("candidates");
        if (candidates == null || candidates.isEmpty()) {
            return "";
        }
        JSONObject c0 = candidates.getJSONObject(0);

        // 3a) Một số bản trả trực tiếp 'text' ở cấp candidate
        String direct = c0.optString("text", "");
        if (!direct.isEmpty()) return direct;

        // 3b) Đường truyền thống: content.parts[].text
        if (c0.has("content")) {
            JSONObject content = c0.getJSONObject("content");
            JSONArray parts = content.optJSONArray("parts");
            if (parts != null && !parts.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < parts.length(); i++) {
                    JSONObject p = parts.getJSONObject(i);
                    String t = p.optString("text", "");
                    if (!t.isEmpty()) {
                        sb.append(t);
                    }
                    // Nếu cần, xử lý inline_data / functionCall tại đây
                }
                String out = sb.toString().trim();
                if (!out.isEmpty()) return out;
            }
        }

        // 3c) Nếu có finishReason, trả thông tin gợi ý
        String finishReason = c0.optString("finishReason", "");
        if (!finishReason.isEmpty()) {
            return "[No text. finishReason=" + finishReason + "]";
        }

        return "";
    }
}
