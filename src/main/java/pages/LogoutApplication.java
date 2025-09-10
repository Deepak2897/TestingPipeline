package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.BaseClass;

public class LogoutApplication  extends BaseClass{
	
    @FindBy(xpath = "//a[text()='Logout']") private WebElement btnLogout;
    @FindBy (xpath = "//td[contains(text(),'You have successfully logged out. ')]") private WebElement logoutmessage;
    
    
  public WebElement getLogoutmessage() {
		return logoutmessage;
	}

  public LogoutApplication(WebDriver driver) {
	  
	  PageFactory.initElements(driver, this);
  }
  
  public void clickLogout() {
	  
	  scrollToTop();
      waitForClickable(btnLogout).click();;
  }

}
