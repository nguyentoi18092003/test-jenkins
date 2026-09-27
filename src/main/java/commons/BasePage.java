package commons;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;



import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasePage {
    public static BasePage getBasePage(){
        return new BasePage();
    }
    //    Web browswer
    public void openPageUrl(WebDriver driver, String pageUrl){
        driver.get(pageUrl);
    }

    public String getPageTile(WebDriver driver){
        return driver.getTitle();

    }
    //tam
    public String getCurrentPageUrl(WebDriver driver){
        return driver.getCurrentUrl();
    }

    public String getPageSourceCode(WebDriver driver){
        return driver.getPageSource();
    }

    public void backToPage(WebDriver driver) {
        driver.navigate().back();
    }

    public void forwordToPage(WebDriver driver){
        driver.navigate().forward();
    }

    public void refreshCurrentPage(WebDriver driver){
        driver.navigate().refresh();
    }

    public Alert waitForAlertPresence(WebDriver driver){
        return new WebDriverWait (driver, Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.alertIsPresent());
    }

    public void acceptToAlert(WebDriver driver){
        waitForAlertPresence(driver).accept();
    }

    public void cancelToAlert(WebDriver driver){
        waitForAlertPresence(driver).dismiss();
    }

    public String getTextInAlert(WebDriver driver){
        return waitForAlertPresence(driver).getText();
    }

    public void sendkeyToAlert(WebDriver driver, String keysToSend){
        waitForAlertPresence(driver).sendKeys(keysToSend);
    }

    public void switchToWindowByID(WebDriver driver,String expectedID){
        //Ham nay ch su dung khi co 2 tab dang duco mo
        //Lay ra ID cua tab hien tai, chu y tat ca cac window/ tab deu co 1 cai ID cau no
        String parentID=driver.getWindowHandle();

        //Lay ra ID cua tat ca cac tab/windown dang duoc mo
        Set<String> allIDs=driver.getWindowHandles();

        //Dung vong lap duyetj qua tung ID
        for(String id:allIDs) {
            //Neu nhu 1 iD nao ma khac voi parentID thi switch vao(TH nay co 2 cai ID thoai)
            if (!id.equals(expectedID)) {//title cac tab k giong nhau ddc nene k lo trung
                driver.switchTo().window(id);
                break;
            }
        }

    }

    public void switchToWindowByTilte(WebDriver driver,String expectedTitle){
        //Lay het tat caa ID cua cac window/tab
        Set<String> allIDs=driver.getWindowHandles();

        //Dung vong lap de duyet qua Set ID o tren
        for(String id:allIDs){
            //cho switch vao tung ID truowc
            driver.switchTo().window(id);
            //Lay ra tile cua tab/window hien tai
            String actualTitle=driver.getTitle();
            if(actualTitle.equals(expectedTitle)){
                break;
            }
        }

    }

    public void closeAllWindowWithoutParent(WebDriver driver,String parentID){
        //Lay het tat caa ID cua cac window/tab
        Set<String> allIDs=driver.getWindowHandles();

        //Dung vong lap de duyet qua Set ID o tren
        for(String id:allIDs){
            if(!id.equals(parentID)){
                driver.switchTo().window(id);
                driver.close();
            }
        }
        driver.switchTo().window(parentID);// chu y van phai co cau lenh chuyene driver ve nhoa
        //sau cuoi cx chi con moi tab cha thoi

    }

    public void sleepInSeconds(long timeInSecond) {
        try {
            Thread.sleep(timeInSecond * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Cookie> getBrowserCookies(WebDriver driver){
        return driver.manage().getCookies();
    }

    public void setCookies(WebDriver driver, Set<Cookie> cookies){
        for(Cookie cookie :cookies){
            driver.manage().addCookie(cookie);
        }
    }

    public void deleteAllCookies(WebDriver driver){
        driver.manage().deleteAllCookies();
    }

    public By getByLocator(String locatorValue){
        By by =null;
        if(locatorValue.startsWith("xpath=") || locatorValue.startsWith("XPath=")|| locatorValue.startsWith("XPATH=")|| locatorValue.startsWith("Xpath=")){
            by=By.xpath(locatorValue.substring(6));
        } else if(locatorValue.startsWith("css=")|| locatorValue.startsWith("Css=")||locatorValue.startsWith("CSS=")){
            by=By.cssSelector(locatorValue.substring(4));
        } else if(locatorValue.startsWith("id=")|| locatorValue.startsWith("Id=")||locatorValue.startsWith("ID=")){
            by=By.id(locatorValue.substring(3));
        }else if(locatorValue.startsWith("name=")|| locatorValue.startsWith("Name=")||locatorValue.startsWith("NAME=")){
            by=By.name(locatorValue.substring(5));
        }else if(locatorValue.startsWith("class=")|| locatorValue.startsWith("Class=")||locatorValue.startsWith("CLASS=")){
            by=By.className(locatorValue.substring(6));
        }else if(locatorValue.startsWith("tagname=")|| locatorValue.startsWith("Tagname=")||locatorValue.startsWith("TAGNAME=")) {
            by = By.tagName(locatorValue.substring(8));
        }else{
            throw new RuntimeException("Locator type í not valid.");
        }
        return by;

    }

    //Web Element
    public By getByXpath(String locator){
        return By.xpath(locator);
    }
    public String getDynamicLocator(String locator, String ... values){
        return String.format(locator,(Object[])values);
    }

    public WebElement getWebElement(WebDriver driver,String locator){
        return driver.findElement(getByLocator(locator));
    }
    public WebElement getWebElement(WebDriver driver,String locator,String ...restParams){
        return driver.findElement(getByLocator(getDynamicLocator(locator,restParams)));
    }

    public List<WebElement> getListWebElement(WebDriver driver, String locator){
        return driver.findElements(getByLocator(locator));
    }

    public List<WebElement> getListWebElement(WebDriver driver, String locator,String ...restParams){
        return driver.findElements(getByLocator(getDynamicLocator(locator,restParams)));
    }


    public  void clickToElement(WebDriver driver, String locator){
        getWebElement(driver,locator).click();
    }
    public  void clickToElement(WebDriver driver, String locator,String ... restParams){
        getWebElement(driver,getDynamicLocator(locator,restParams)).click();
    }
    public  void clickToElement(WebDriver driver, WebElement element){
        element.click();
    }


    public void sendkeyToElement(WebDriver driver, String locator, String valueToSend){
        getWebElement(driver,locator).clear();
        getWebElement(driver,locator).sendKeys(valueToSend);
    }

    public void sendkeyToElement(WebDriver driver, String locator, String valueToSend,String ... restParams){
        getWebElement(driver,getDynamicLocator(locator,restParams)).clear();
        getWebElement(driver,getDynamicLocator(locator,restParams)).sendKeys(valueToSend);
        System.out.println(getDynamicLocator(locator,restParams));
    }

    public void selectItemInDefaultDropdown(WebDriver driver, String locator, String itemValue){
        new Select(getWebElement(driver,locator)).selectByVisibleText(itemValue);
    }
    public void selectItemInDefaultDropdown(WebDriver driver, String locator, String itemValue, String... restParams){
        new Select(getWebElement(driver,getDynamicLocator(locator,restParams))).selectByVisibleText(itemValue);
    }

    public String getFirstSelectedTextInDefaultDropdown(WebDriver driver,String locator){
        return new Select(getWebElement(driver,locator)).getFirstSelectedOption().getText();
    }

    public boolean isDefaultDropdownMultiple(WebDriver driver,String locator){
        return new Select(getWebElement(driver,locator)).isMultiple();
    }

    public void selectItemDropdown(WebDriver driver,String parentLocator, String childLocator, String itemTextExpected) {
        getWebElement(driver,parentLocator).click();
        sleepInSeconds(1);
        List<WebElement> allItems = new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.
                presenceOfAllElementsLocatedBy(getByLocator(childLocator)));

        for (WebElement item : allItems) {
            String textItems = item.getText();
            if (textItems.equals(itemTextExpected)) {
                item.click();
                break;
            }
        }
    }
    public void selectItemDropdown(WebDriver driver,String parentLocator, String childLocator, String itemTextExpected,String... restParams) {
        getWebElement(driver,parentLocator,restParams).click();
        sleepInSeconds(1);
        List<WebElement> allItems = new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.
                presenceOfAllElementsLocatedBy(getByLocator(getDynamicLocator(childLocator,restParams))));

        for (WebElement item : allItems) {
            String textItems = item.getText();
            if (textItems.equals(itemTextExpected)) {
                item.click();
                break;
            }
        }
    }
    public void selectItemInCombobox(WebDriver driver,String locator,String... restParams){
        waitForElementClickable(driver,locator, restParams);
        new Actions(driver)
                .sendKeys(Keys.ARROW_DOWN) // chọn suggestion đầu tiên
                .sendKeys(Keys.ENTER)
                .perform();
    }

    public String getElementText(WebDriver driver,String locator){
        return getWebElement(driver,locator).getText();
    }

    public String getElementText(WebDriver driver,String locator,String ... restParams){
        return getWebElement(driver,getDynamicLocator(locator,restParams)).getText();
    }
    public List<String> getListElementText(WebDriver driver,String locator){
        List<String> texts=new ArrayList<>();
        List<WebElement> elements=getListWebElement(driver, locator);
        for(WebElement x:elements){
            texts.add(x.getText());
        }
        return texts;
    }
    public List <String> getListElementAttribute(WebDriver driver,String locator,String attributeName){
        List<String> listAttributeValue=new ArrayList<>();
        List<WebElement> elements=getListWebElement(driver, locator);
        for(WebElement x:elements){
            listAttributeValue.add(x.getAttribute(attributeName));
        }
        return listAttributeValue;
    }
    public List <String> getListElementCssValue(WebDriver driver,String locator,String cssPropertyName){
        List<String> listAttributeValue=new ArrayList<>();
        List<WebElement> elements=getListWebElement(driver, locator);
        for(WebElement x:elements){
            listAttributeValue.add(x.getCssValue(cssPropertyName));
        }
        return listAttributeValue;
    }

    public String getElementAttribute(WebDriver driver,String locator,String attributeName){
        return getWebElement(driver,locator).getAttribute(attributeName);
    }

    public String getElementAttribute(WebDriver driver,String locator,String attributeName,String ... restParams){
        return getWebElement(driver,getDynamicLocator(locator,restParams)).getAttribute(attributeName);
    }

    public String getElementCssValue(WebDriver driver,String locator,String cssPropertyName){
        return getWebElement(driver,locator).getCssValue(cssPropertyName);
    }
    public String getElementCssValue(WebDriver driver,String locator,String cssPropertyName,String ... restParams){
        return getWebElement(driver,getDynamicLocator(locator,restParams)).getCssValue(cssPropertyName);
    }


    public String convertRGBAToHexaColor(WebDriver driver,String locator){
        return Color.fromString(getElementCssValue(driver,locator,"background-color")).asHex();
    }

    public int getListElementSize(WebDriver driver,String locator){
        return getListWebElement(driver,locator).size();
    }

    public int getListElementSize(WebDriver driver,String locator, String ...restParams){
        return getListWebElement(driver,getDynamicLocator(locator,restParams)).size();
    }
    //Apply for checkbox and radio button
    public void checkToElement(WebDriver driver, String locator){
        if(!getWebElement(driver,locator).isSelected()){
            getWebElement(driver,locator).click();
        }
    }
    public void checkToElement(WebDriver driver, String locator,String ...restParams){
        if(!getWebElement(driver,getDynamicLocator(locator,restParams)).isSelected()){
            getWebElement(driver,getDynamicLocator(locator,restParams)).click();
        }
    }
    //only apply for checkbox
    public void uncheckToElement(WebDriver driver, String locator){
        if(!getWebElement(driver,locator).isSelected()){
            getWebElement(driver,locator).click();
        }
    }
    //case 1: ELement hien thi va co trong HTML
    //case 2: ELement khong hien thi va co trong HTML, vi trong ham nay co sd den findELement nen co trong HTMl la ok
    public boolean isElementDisplayed(WebDriver driver, String locator){
        return getWebElement(driver,locator).isDisplayed();
    }

    public void setImplicitWait(WebDriver driver, long timeout){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));

    }
    //K co tren UI va k co trong HTMl
    public boolean isElementUndisplayed(WebDriver driver, String locator){
        //Truoc khi tim element thi set time ngan thoi
        setImplicitWait(driver,shortTimeOut);
        List<WebElement> elements=getListWebElement(driver,locator);
        //Tra lai timeout mac dinh cho cac step con lai
        setImplicitWait(driver,longTimeOut);
        if(elements.size()>0 && elements.get(0).isDisplayed()){
            return false;
        }
        else if(elements.size()>0 && !elements.get(0).isDisplayed()){//Element khong co trong UI va co trong DOM
            return true;
        }
        else{//Element khong co trong UI va khong co trong dom luon
            return true;
        }

    }

    public boolean isElementDisplayed(WebDriver driver, String locator,String ...restParams){
        return getWebElement(driver,getDynamicLocator(locator,restParams)).isDisplayed();
    }

    public boolean isElementSelected(WebDriver driver,String locator){
        return getWebElement(driver,locator).isSelected();
    }
    public boolean isElementSelected(WebDriver driver,String locator,String ...restParams){
        return getWebElement(driver,getDynamicLocator(locator,restParams)).isSelected();
    }

    public boolean isElementEnable(WebDriver driver,String locator){
        return getWebElement(driver,locator).isEnabled();
    }
    public boolean isElementEnable(WebDriver driver,String locator,String ...restParams){
        return getWebElement(driver,getDynamicLocator(locator,restParams)).isEnabled();
    }

    public void switchToIframe(WebDriver driver,String locator){
        new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(getByLocator(locator)));//vua wait vua switch luon
    }

    public void switchToDefaultContent(WebDriver driver){
        driver.switchTo().defaultContent();//swith ve trang truoc do(trang truoc khi mk switch vao)
    }

    public void hoverToElement(WebDriver driver, String locator){
        new Actions(driver).moveToElement(getWebElement(driver,locator)).perform();
    }

    public void doubleClickElement(WebDriver driver,String locator){
        new Actions(driver).doubleClick(getWebElement(driver,locator));
    }

    public void  rightClickElement(WebDriver driver,String locator){
        new Actions(driver).contextClick(getWebElement(driver,locator));
    }

    public void dragAndDropElement(WebDriver driver,String sourceLocator, String targetLocator){
        new Actions(driver).dragAndDrop(getWebElement(driver,sourceLocator),getWebElement(driver,targetLocator)).perform();
    }

    public void sendKeyBoardToElement(WebDriver driver, String locator, Keys key){
        new Actions(driver).sendKeys(getWebElement(driver,locator),key).perform();
    }

    public Object executeForBrowser(WebDriver driver,String javaScript) {
        return ((JavascriptExecutor) driver).executeScript(javaScript);
    }

    public String getInnerText(WebDriver driver) {
        return (String) ((JavascriptExecutor) driver).executeScript("return document.documentElement.innerText;");
    }

    public void sleepInSecond(int timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean areExpectedTextInInnerText(WebDriver driver,String textExpected) {
        String textActual = (String) ((JavascriptExecutor) driver).executeScript("return document.documentElement.innerText.match('" + textExpected + "')[0];");
        return textActual.equals(textExpected);
    }

    public void scrollToBottomPage(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void navigateToUrlByJS(WebDriver driver,String url) {
        ((JavascriptExecutor) driver).executeScript("window.location = '" + url + "'");

    }

    public void hightlightElement(WebDriver driver,String locator) {
        WebElement element = getWebElement(driver,locator);
        String originalStyle = element.getAttribute("style");
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', arguments[1])", element, "border: 2px solid red; border-style: dashed;");
        sleepInSecond(2);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', arguments[1])", element, originalStyle);
    }
    //Chi can Element xuat hiẹn trong DOM la click dc khong nhat thiet la phai click xuat hien tren man hinh(Kieu dung trong TH phan tu bi che khuat i)
    public void clickToElementByJS(WebDriver driver,String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getWebElement(driver,locator));
        sleepInSecond(3);
    }
    public void clickToElementByJS(WebDriver driver,String locator,String ...restParams) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getWebElement(driver,getDynamicLocator(locator,restParams)));
        sleepInSecond(3);
    }

    public void scrollToElementOnTop(WebDriver driver,String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver,locator));
    }
    public void scrollToElementOnTop(WebDriver driver,String locator,String ...restParams) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver,getDynamicLocator(locator,restParams)));
    }

    public void scrollToElementOnDown(WebDriver driver,String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", getWebElement(driver,locator));
    }
    public void scrollToElementOnDown(WebDriver driver,String locator,String ...restParams) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", getWebElement(driver,getDynamicLocator(locator,restParams)));
    }

//    public void setAttributeInDOM(WebDriver driver,String locator, String attributeName, String attributeValue) {
//        jsExecutor.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + attributeValue +"');", getWebElement(locator));
//    }

    public void removeAttributeInDOM(WebDriver driver,String locator, String attributeRemove) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getWebElement(driver,locator));
    }


    public void sendkeyToElementByJS(WebDriver driver,String locator, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '" + value + "')", getWebElement(driver,locator));
    }

//    public String getAttributeInDOM(WebDriver driver,String locator, String attributeName) {
//        return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].getAttribute('" + attributeName + "');", getWebElement(driver,locator));
//    }

    public String getElementValidationMessage(WebDriver driver,String locator) {
        return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].validationMessage;", getWebElement(driver,locator));
    }

    public boolean isImageLoaded(WebDriver driver,String locator) {
        return (boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0", getWebElement(driver,locator));

    }

    public void waitForElementVisible(WebDriver driver,String locator){
        new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }

    public void waitForElementVisible(WebDriver driver,String locator,String ...restParams){
        new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.visibilityOfElementLocated(getByLocator(getDynamicLocator(locator,restParams))));
    }

    public void waitForListElementVisible(WebDriver driver,String locator){
        new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.visibilityOfAllElements(getListWebElement(driver,locator)));
    }

    public void waitForElementInvisible(WebDriver driver,String locator){
        new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
    }

    public void waitForListElementInvisible(WebDriver driver,String locator){
        new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver,locator)));
    }

    protected void waitForElementClickable(WebDriver driver, String locator){
        new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.elementToBeClickable(getWebElement(driver,locator)));
    }

    protected void waitForElementClickable(WebDriver driver, WebElement element){
        new WebDriverWait(driver,Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.elementToBeClickable(element));
    }


    protected void waitForElementClickable(WebDriver driver, String locator,String ...restParams){
        new WebDriverWait(driver, Duration.ofSeconds(longTimeOut)).until(ExpectedConditions.elementToBeClickable(getWebElement(driver,getDynamicLocator(locator,restParams))));
    }
    public boolean isPageLoadedSuccess(WebDriver driver) {
        return (new WebDriverWait(driver, Duration.ofSeconds(longTimeOut))).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean)((JavascriptExecutor) driver)
                        .executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        });
    }

//    public void uploadMultipleFiles(WebDriver driver,String ...fileNames){
//        String filePath=GlobalConstants.getGlobalConstants().getUploadFolderPath();
//        String fullFileName="";
//        for(String file:fileNames){
//            fullFileName=fullFileName+filePath+file+"\n";//can \n mk ms upload dc nhieu file xem them trong phan cu
//        }
//        fullFileName=fullFileName.trim();// ham trim de xoa \n o dau va cuoi
//        getWebElement(driver, BaseElementUI.UPLOAD_FILE_TYPE).sendKeys(fullFileName);
//    }
    // not written by Dao Minh Dam
    public static String getValueAfterEqual(String input) {
        Pattern pattern = Pattern.compile("=(.*)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1); // Lấy nội dung sau dấu '='
        }
        return null;
    }
    // Hàm xóa input bằng JavaScript

    public void clearInputByJS(WebDriver driver, String locator, String... restParams) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Thay thế %s nếu có tham số truyền vào
        if (restParams.length > 0) {
            locator = String.format(locator, (Object[]) restParams);
        }

        locator = getValueAfterEqual(locator); // Lấy giá trị sau dấu "="

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement element;
        if (locator.startsWith("//")) {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
        } else {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
        }

        // Scroll tới phần tử để đảm bảo có thể thao tác
        js.executeScript("arguments[0].scrollIntoView(true);", element);

        try {
            // Cách 1: Xóa giá trị bằng JavaScript (ưu tiên)
            js.executeScript("arguments[0].value = '';", element);

            // Cách 2: Nếu cách trên không được, dùng `setAttribute`
            js.executeScript("arguments[0].setAttribute('value', '');", element);

            // Cách 3: Nếu JavaScript không có tác dụng, dùng phím tắt (CTRL + A, BACKSPACE)
            element.sendKeys(Keys.CONTROL + "a");
            element.sendKeys(Keys.BACK_SPACE);

            System.out.println("✅ Xóa thành công giá trị trong ô input: " + locator);
        } catch (Exception e) {
            System.out.println("❌ Không thể xóa giá trị: " + e.getMessage());
        }
    }

    //write by Toi not Dao Minh Dam
    public String getValueEventElementByJS(WebDriver driver, String locator){
        WebElement el=getWebElement(driver,locator);
        String val = el.getDomProperty("value");
        return val;
    }



    private long longTimeOut=GlobalConstants.getGlobalConstants().getLongTimeout();
    private long shortTimeOut=GlobalConstants.getGlobalInstance().getShortTimeout();


}

