package commons;




import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Locale;
import java.util.Random;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;

import java.util.ArrayList;
import java.util.List;
@Listeners(BaseTest.TestTypeFilter.class)
public class BaseTest {
    private WebDriver driver;

    public BaseTest(){

    }
    protected WebDriver getBrowserDriver(String browserName){
        BrowserList browser=BrowserList.valueOf(browserName.toUpperCase());
        if(browser==BrowserList.FIREFOX){
            driver=new FirefoxDriver();
        }
        else if(browser==BrowserList.CHROME){
            driver=new ChromeDriver();
        }
        else if(browser==BrowserList.EDGE){
            driver=new EdgeDriver();
        }
        else {
            throw new RuntimeException("Ten trinh duyet khong hop le");
        }


        //set kich thuoc man hinh cua trinh duyet khi chay
        driver.manage().window().setPosition(new Point(0,0));
        //driver.manage().window().setSize(new Dimension(1024,768));
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));


        driver.get("https://www.youtube.com/");
        return driver;
    }

    protected WebDriver getBrowserDriver(String browserName,String url){
        // Uu tien gia tri tu ben ngoai (-Dbrowser, -Durl), khong co thi dung gia tri trong file xml
        String browserFromOutside = System.getProperty("browser");
        if (browserFromOutside != null && !browserFromOutside.isBlank()) {
            browserName = browserFromOutside;
        }

        String urlFromOutside = System.getProperty("url");
        if (urlFromOutside != null && !urlFromOutside.isBlank()) {
            url = urlFromOutside;
        }

        System.out.println(">>> Chay voi browser = " + browserName + " | url = " + url);
        BrowserList browser=BrowserList.valueOf(browserName.toUpperCase());
        if(browser==BrowserList.FIREFOX){
            driver=new FirefoxDriver();
        }
        else if(browser==BrowserList.CHROME){
            driver=new ChromeDriver();
        }
        else if(browser==BrowserList.EDGE){
            driver=new EdgeDriver();
        }
        else {
            throw new RuntimeException("Ten trinh duyet khong hop le");
        }


        //set kich thuoc man hinh cua trinh duyet khi chay
        driver.manage().window().setPosition(new Point(0,0));
        //driver.manage().window().setSize(new Dimension(1024,768));
        driver.manage().window().maximize();


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(GlobalConstants.getGlobalConstants().getLongTimeout()));


        driver.get(url);
        return driver;
    }

    public String getEmailRadom() {
        Random rand=new Random();
        return "john"+ rand.nextInt(99999)+ "@kennedy.us";
    }
    //Ham nay dung de dong browswer va dong ca ben trong task manager nua, (cho du mk co cho them ca anntion always run=trueo ben Testcase thi no cx chi dong dc trinh duyet nhung k dong dc trong task manager nene phai voet tham ham nay nua)
    protected void closeBrowser(){
        String cmd = null;
        try {
            String osName = GlobalConstants.getGlobalConstants().getOs_name().toLowerCase();

            String driverInstanceName = driver.toString().toLowerCase();

            String browserDriverName = null;

            if (driverInstanceName.contains("chrome")) {
                browserDriverName = "chromedriver";
            } else if (driverInstanceName.contains("internetexplorer")) {
                browserDriverName = "IEDriverServer";
            } else if (driverInstanceName.contains("firefox")) {
                browserDriverName = "geckodriver";
            } else if (driverInstanceName.contains("edge")) {
                browserDriverName = "msedgedriver";
            } else if (driverInstanceName.contains("opera")) {
                browserDriverName = "operadriver";
            } else {
                browserDriverName = "safaridriver";
            }

            if (osName.contains("window")) {
                cmd = "taskkill /F /FI 'IMAGENAME eq " + browserDriverName + "*'";//cau lenh nay dung de dong process
            } else {
                cmd = "pkill " + browserDriverName;
            }
//1- Close browser
            if (driver != null) {
                driver.manage().deleteAllCookies();
                driver.quit();
            }
        } catch (Exception e) {
        } finally {
            //2- Quit driver (executable)
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //3 ham duoi day la copy tu file cua thay va dung cho phan report loi

    protected boolean verifyTrue(boolean condition) {
        boolean pass = true;
        try {

            Assert.assertTrue(condition);
        } catch (Throwable e) {
            pass = false;

            // Add lỗi vào ReportNG
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }


    protected boolean verifyFalse(boolean condition) {
        boolean pass = true;
        try {
            Assert.assertFalse(condition);
        } catch (Throwable e) {

            pass = false;
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }


    protected boolean verifyEquals(Object actual, Object expected) {
        boolean pass = true;
        try {
            Assert.assertEquals(actual, expected);

        } catch (Throwable e) {
            pass = false;
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    //Ham nay de dung cho viec cau hinh cho reportNG
    public WebDriver getDriver(){
        return driver;
    }
    @BeforeSuite
    public void deleteFileInReportNG() {
        //log.info("Starting delete all file in ReportNG screenshot");
        // Remove all file in ReportNG screenshot (image)
        deleteAllFileInFolder("reportNGImage");
        //log.info("Deleted success");
        deleteAllFileInFolder("allure-json");

    }
    public void deleteAllFileInFolder(String folderName) {
        try {
            String pathFolderDownload = GlobalConstants.getGlobalConstants().getProjectPath()+File.separator+folderName;
            File file = new File(pathFolderDownload);
            File[] listOfFiles = file.listFiles();
            if (listOfFiles.length != 0) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("environment.properties")) {
                        new File(listOfFiles[i].toString()).delete();
                    }
                }
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
    public static class TestTypeFilter implements IMethodInterceptor {
        @Override
        public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
            String testType = System.getProperty("testType");
            String testClass = System.getProperty("testClass");

            List<IMethodInstance> result = new ArrayList<>();
            for (IMethodInstance m : methods) {
                if (matchClass(m, testClass) && matchType(m, testType)) {
                    result.add(m);
                }
            }

            System.out.println(">>> Che do chay: " + (isAll(testType) ? "ALL" : testType)
                    + " | Class: " + (isAll(testClass) ? "ALL" : testClass)
                    + " | So testcase: " + result.size());
            return result;
        }

        private boolean isAll(String value) {
            return value == null || value.isBlank() || value.equalsIgnoreCase("all");
        }

        private boolean matchType(IMethodInstance m, String testType) {
            if (isAll(testType)) return true;
            for (String group : m.getMethod().getGroups()) {
                if (group.equalsIgnoreCase(testType)) return true;
            }
            return false;
        }

        private boolean matchClass(IMethodInstance m, String testClass) {
            if (isAll(testClass)) return true;
            Class<?> clazz = m.getMethod().getRealClass();
            for (String name : testClass.split(",")) {
                String n = name.trim();
                if (n.equalsIgnoreCase(clazz.getSimpleName()) || n.equalsIgnoreCase(clazz.getName())) {
                    return true;
                }
            }
            return false;
        }
    }
}