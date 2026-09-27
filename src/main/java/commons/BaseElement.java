package commons;

import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class BaseElement extends BasePage {
    WebDriver driver;

    public BaseElement(WebDriver driver) {
        this.driver = driver;
    }


}