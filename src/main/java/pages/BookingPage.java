package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import base.BaseClass;

public class BookingPage extends BaseClass {

    @FindBy(id = "location") private WebElement dropdownLocation;
    @FindBy(id = "hotels") private WebElement dropdownHotel;
    @FindBy(id = "room_type") private WebElement dropdownRoomType;
    @FindBy(id = "room_nos") private WebElement dropdownRoomNos;
    @FindBy(id = "Submit") private WebElement btnSearch;
    @FindBy(id = "radiobutton_0") private WebElement radioSelectHotel;
    @FindBy(id = "continue") private WebElement btnContinue;

    public BookingPage(WebDriver driver) { PageFactory.initElements(driver, this); }

    public void searchHotel(String location, String hotel, String roomType, String roomNos) {
        selectByVisibleText(dropdownLocation, location);
        selectByVisibleText(dropdownHotel, hotel);
        selectByVisibleText(dropdownRoomType, roomType);
        selectByVisibleText(dropdownRoomNos, roomNos);
        clickElement(btnSearch);
        clickElement(radioSelectHotel);
        clickElement(btnContinue);
    }
}
