package commons;

import java.io.File;

public class GlobalConstants {
//---------------Đống này lúc khởi tạo framwork+ report là có luôn rồi------------
//    public static final long SHORT_TIMEOUT=5;
//    public static final long LONG_TIMEOUT=30;
//
//    public static final String OS_NAME=System.getProperty("os.name");
//    public static final String RELATIVE_PROJECT_PATH=System.getProperty("user.dir");
//    public static final String UPLOAD_PATH=RELATIVE_PROJECT_PATH+ File.separator+"uploadFiles"+File.separator;//File.separator no se tu biet lauy "\" doi vs window vaf "/" vs mac
//    public static final String DOWNLOAD_PATH=RELATIVE_PROJECT_PATH+File.separator+"downloadFiles"+ File.separator;
//    public static final String REPORT_IMAGE_PATH=RELATIVE_PROJECT_PATH+File.separator+"reportNGImage"+File.separator;
//
//    public static final String JAVA_VERSION = System.getProperty("java.version");
    //---------------Đống này lúc khởi tạo framwork+ report là có luôn rồi------------
private static GlobalConstants globalInstance;
    private GlobalConstants(){}
    public static synchronized  GlobalConstants getGlobalConstants(){
        if(globalInstance==null){
            globalInstance=new GlobalConstants();
        }
        return globalInstance;
    }
    private final long shortTimeout=5;
    private final long longTimeout=30;
    private final String projectPath=System.getProperty("user.dir");
    private final String os_name=System.getProperty("os.name");
    private final String uploadFolderPath=projectPath +File.separator+"uploadFiles"+File.separator;
    private final String downloadPath=projectPath+File.separator+"downloadFiles";
    private final String reportImagePath=projectPath +File.separator+"reportNGImage"+File.separator;
    private final String javaVersion =System.getProperty("java.version");
    public  final String dataTestPath=projectPath+File.separator+"src"+File.separator+"testdata"+File.separator+File.separator;
    public static GlobalConstants getGlobalInstance() {
        return globalInstance;
    }

    public long getShortTimeout() {
        return shortTimeout;
    }

    public long getLongTimeout() {
        return longTimeout;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getOs_name() {
        return os_name;
    }

    public String getUploadFolderPath() {
        return uploadFolderPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public String getReportImagePath() {
        return reportImagePath;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public String getDataTestPath() {
        return dataTestPath;
    }

}
