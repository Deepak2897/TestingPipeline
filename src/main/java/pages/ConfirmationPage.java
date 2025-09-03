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
    @FindBy(id = "logout") private WebElement btnLogout;

    public ConfirmationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public String getOrderNumber() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(txtOrderNo));

        return txtOrderNo.getAttribute("value");
    }

    public void clickLogout() {
        clickElement(btnLogout);
    }
}
