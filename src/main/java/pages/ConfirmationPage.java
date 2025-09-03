package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import base.BaseClass;

public class ConfirmationPage extends BaseClass {

    @FindBy(id = "order_no") private WebElement txtOrderNo;
    @FindBy(id = "logout") private WebElement btnLogout;

    public ConfirmationPage(WebDriver driver) 
    {
    	PageFactory.initElements(driver, this);
    
    }

    public String getOrderNumber()  { 
    	
        WebElement orderElement = waitForVisibility(txtOrderNo, 10); // wait up to 10s
    	return txtOrderNo.getAttribute("value"); 
    	}
    
    public void clickLogout() { 
    	
    	clickElement(btnLogout); 
    	
    }
}
