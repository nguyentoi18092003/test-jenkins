import commons.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import reportConfig.ExtentTestManager;

import java.lang.reflect.Method;

public class testJenkins extends BaseTest {

    private WebDriver driver;
    private String browserName;
    private pageObjects.testJenkins youtubePage;

    @Parameters({"url", "browser"})
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(String url, String browserName) {
        driver = getBrowserDriver(browserName, url);
        this.browserName = browserName;
        youtubePage = new pageObjects.testJenkins(driver);
    }

    @Test(groups = "regression")
    public void TC_01_Verify_Youtube_Logo_Displayed(Method method) {
        ExtentTestManager.startTest(method.getName() + "-" + browserName.toUpperCase(), "Verify logo Youtube hien thi");
        Assert.assertTrue(youtubePage.verifyTitleDisplay(), "Logo Youtube khong hien thi");
    }

    @Test(groups = "regression")
    public void TC_02_Verify_Youtube_Logo_Displayed(Method method) {
        ExtentTestManager.startTest(method.getName() + "-" + browserName.toUpperCase(), "Verify logo Youtube hien thi");
        Assert.assertTrue(youtubePage.verifyTitleDisplay(), "Logo Youtube khong hien thi");
    }

    @Test(groups = "regression")
    public void TC_03_Verify_Youtube_Logo_Displayed(Method method) {
        ExtentTestManager.startTest(method.getName() + "-" + browserName.toUpperCase(), "Verify logo Youtube hien thi");
        Assert.assertTrue(youtubePage.verifyTitleDisplay(), "Logo Youtube khong hien thi");
    }
    @Test(groups = "smoke")
    public void TC_04_Verify_Youtube_Logo_Displayed(Method method) {
        ExtentTestManager.startTest(method.getName() + "-" + browserName.toUpperCase(), "Verify logo Youtube hien thi");
        Assert.assertTrue(youtubePage.verifyTitleDisplay(), "Logo Youtube khong hien thi");
    }
    @Test(groups = "smoke")
    public void TC_05_Verify_Youtube_Logo_Displayed(Method method) {
        ExtentTestManager.startTest(method.getName() + "-" + browserName.toUpperCase(), "Verify logo Youtube hien thi");
        Assert.assertTrue(youtubePage.verifyTitleDisplay(), "Logo Youtube khong hien thi");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeBrowser();
    }
}