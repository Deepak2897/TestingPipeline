package base;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class BaseClass {

    public static WebDriver driver;
    public static com.aventstack.extentreports.ExtentReports extent;
    public static com.aventstack.extentreports.ExtentTest test;

    // Launch browser
    public WebDriver launchBrowser(String url) {
    	WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
        return driver;
    }

    // Close browser
    public void closeBrowser() {
        if(driver != null) driver.quit();
    }

    // Click element
    public void clickElement(WebElement element) {
    	element.click(); }

    // Enter text
    public void enterText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    // Select dropdown by visible text
    public void selectByVisibleText(WebElement element, String text) {
        new Select(element).selectByVisibleText(text);
    }

    //take screenshot
   
        public String takeScreenshot(String testName) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String path = System.getProperty("user.dir") + "/test-output/screenshots/" + testName + "_" + timestamp + ".png";

            // Create the directory if it does not exist
            File screenshotDir = new File(System.getProperty("user.dir") + "/test-output/screenshots/");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            File dest = new File(path);
            try {
                FileUtils.copyFile(src, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return path;
        }

    }


