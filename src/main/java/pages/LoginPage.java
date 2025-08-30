package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import base.BaseClass;

public class LoginPage extends BaseClass {

    @FindBy(id = "username") private WebElement txtUsername;
    @FindBy(id = "password") private WebElement txtPassword;
    @FindBy(id = "login") private WebElement btnLogin;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        enterText(txtUsername, username);
        enterText(txtPassword, password);
        clickElement(btnLogin);
    }
}
