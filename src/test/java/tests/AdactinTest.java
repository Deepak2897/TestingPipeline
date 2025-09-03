package tests;

import org.testng.annotations.*;
import base.BaseClass;
import pages.LoginPage;
import pages.BookingPage;
import pages.PaymentPage;
import pages.ConfirmationPage;

public class AdactinTest extends BaseClass {

    LoginPage loginPage;
    BookingPage bookingPage;
    PaymentPage paymentPage;
    ConfirmationPage confirmationPage;

    @BeforeClass
    public void setup() {
    	driver = launchBrowser("https://adactinhotelapp.com/");
    	}

    @Test
    public void bookHotelTest() {
        loginPage = new LoginPage(driver);
        loginPage.login("Deepak2897", "Deepak@2897");

        bookingPage = new BookingPage(driver);
        bookingPage.searchHotel("Sydney", "Hotel Creek", "Standard", "1 - One");

        paymentPage = new PaymentPage(driver);
        paymentPage.enterPaymentDetails("Deepak", "k", "123 Street, Sydney",
                                        "4111111111111111", "VISA", "January", "2026", "123");
        

        confirmationPage = new ConfirmationPage(driver);
        String orderNo = confirmationPage.getOrderNumber();
        System.out.println("Booking ID: " + orderNo);
        

        confirmationPage.clickLogout();
    }

    @AfterClass
    public void tearDown() { closeBrowser(); }
}
