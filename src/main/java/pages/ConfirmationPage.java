package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import base.BaseClass;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ConfirmationPage extends BaseClass {

    @FindBy(id = "order_no") private WebElement txtOrderNo;

    public ConfirmationPage(WebDriver driver) {
        PageFactory.initElements(getDriver(), this);
    }

    public String getOrderNumber() {
        
        waitForVisible(txtOrderNo);

        return txtOrderNo.getAttribute("value");
    }

    
}
