package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import base.BaseClass;

public class PaymentPage extends BaseClass {

    @FindBy(id = "first_name") private WebElement txtFirstName;
    @FindBy(id = "last_name") private WebElement txtLastName;
    @FindBy(id = "address") private WebElement txtAddress;
    @FindBy(id = "cc_num") private WebElement txtCCNum;
    @FindBy(id = "cc_type") private WebElement dropdownCCType;
    @FindBy(id = "cc_exp_month") private WebElement dropdownExpMonth;
    @FindBy(id = "cc_exp_year") private WebElement dropdownExpYear;
    @FindBy(id = "cc_cvv") private WebElement txtCVV;
    @FindBy(id = "book_now") private WebElement btnBookNow;

    public PaymentPage(WebDriver driver)  {
    	
    	PageFactory.initElements(getDriver(), this);
    }

    public void enterPaymentDetails(String fname, String lname, String address,
                                    String ccNo, String ccType, String expMonth,
                                    String expYear, String cvv) {
        enterData(txtFirstName, fname);
        enterData(txtLastName, lname);
        enterData(txtAddress, address);
        enterData(txtCCNum, ccNo);
        selectByVisibilityText(dropdownCCType, ccType);
        selectByVisibilityText(dropdownExpMonth, expMonth);
        selectByVisibilityText(dropdownExpYear, expYear);
        enterData(txtCVV, cvv);
        waitForClickable(btnBookNow).click();; 
    }
}
