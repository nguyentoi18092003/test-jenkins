

import commons.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import reportConfig.ExtentTestManager;

import java.lang.reflect.Method;

public class testJenkins extends BaseTest {

    private WebDriver driver;
    private String browserName;
    private pageObjects.testJenkins youtubePage;

    @Parameters({"url", "browser"})
    @BeforeClass
    public void beforeClass(String url, String browserName) {
        driver = getBrowserDriver(browserName, url);
        this.browserName = browserName;
        youtubePage = new pageObjects.testJenkins(driver);
    }

    @Test
    public void TC_01_Verify_Youtube_Logo_Displayed(Method method) {
        ExtentTestManager.startTest(method.getName() + "-" + browserName.toUpperCase(), "Verify logo Youtube hien thi");
        Assert.assertTrue(youtubePage.verifyTitleDisplay(), "Logo Youtube khong hien thi");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        closeBrowser();
    }
}