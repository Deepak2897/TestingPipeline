package tests; 
import org.testng.Assert; 
import org.testng.annotations.*; 
import base.BaseClass;
import pages.LoginPage;
import pages.LogoutApplication;
import pages.BookingPage; 
import pages.PaymentPage; 
import pages.ConfirmationPage; 


public class AdactinTest extends BaseClass { 
	LoginPage loginPage; 
	BookingPage bookingPage; 
	PaymentPage paymentPage; 
	ConfirmationPage confirmationPage; 
	LogoutApplication logout;
	
	@BeforeClass 
	public void setup() { 
		
		launchBrowser();
		loginPage = new LoginPage(getDriver());
		bookingPage = new BookingPage(getDriver());
		paymentPage = new PaymentPage(getDriver()); 
		confirmationPage = new ConfirmationPage(getDriver());
		logout = new LogoutApplication(getDriver());
	
		}
	
	
	@Test 
	public void logincreds() {
		loginPage.login("Deepak2897", "Deepak@2897");
		waitForVisible(loginPage.getAppLogo());
		
		String actualTitle = getPageTitle();
		System.out.println("Page Title is:"+actualTitle);
		Assert.assertTrue(actualTitle.toLowerCase().contains("adactin"),
		    "Expected page title to contain 'Adactin' but was: " + actualTitle);

		
	}
	
	@Test (dependsOnMethods = {"logincreds"})
		public void bookingpagedetails() {
		
		bookingPage.searchHotel("Sydney", "Hotel Creek", "Standard", "1 - One");
		}
	
	@Test (dependsOnMethods = {"bookingpagedetails"})

	public void confirmbookingpage() {

		paymentPage.enterPaymentDetails("Johnny", "Doe", "123 Street, Sydney", "4111111111111111", "VISA", "January", "2026", "123");
		
		String orderNo = confirmationPage.getOrderNumber();
		// ✅ Extra safety Assert.assertNotNull(orderNo, "Order number should not be null");
		System.out.println("Booking ID: " + orderNo); 
		
		}
	
	@Test  (dependsOnMethods = {"confirmbookingpage"})
	public void logoutSuccessfully() {
		
		logout.clickLogout();
		waitForVisible(logout.getLogoutmessage());
		String LogoutTitle = getPageTitle();
		System.out.println("Page Title:"+LogoutTitle);
		System.out.println("Logged Out");


	}
	

   @AfterClass public void tearDown() {
	   closeBrowser(); 
	   }
   }
