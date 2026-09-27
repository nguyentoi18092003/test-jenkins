package pageObjects;

import commons.BasePage;
import org.openqa.selenium.WebDriver;


import static pageUIs.testJenkinsUI.TITLE_YOUTUBE;

public class testJenkins extends BasePage {
    WebDriver driver;

    public testJenkins(WebDriver driver) {
        this.driver = driver;
    }

    public boolean verifyTitleDisplay() {
        return isElementDisplayed(driver, TITLE_YOUTUBE);
    }
}