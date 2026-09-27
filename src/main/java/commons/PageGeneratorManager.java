package commons;

import org.openqa.selenium.WebDriver;
import pageObjects.testJenkins;


public class PageGeneratorManager {

    public static testJenkins getTestJenkinsPage(WebDriver driver){
        return new testJenkins(driver);
    }



}
