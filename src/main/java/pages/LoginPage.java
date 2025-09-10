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
	@FindBy(xpath = "//img[contains(@class,'logo')]") private WebElement appLogo;	
	public LoginPage(WebDriver driver) {
		
		PageFactory.initElements(getDriver(), this);
		}
	
	
	public WebElement getAppLogo() {
		return appLogo;
	}
	
	public void login(String username, String password) {
	
		enterData(txtUsername, username);
		enterData(txtPassword, password);
		waitForClickable(btnLogin).click();
		}
	}