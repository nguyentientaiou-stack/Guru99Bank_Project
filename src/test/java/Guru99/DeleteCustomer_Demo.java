package Guru99;

import static org.testng.AssertJUnit.assertTrue;

import common.keywords.WebUI;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DeleteCustomer_Page;
import pages.Login_Page;
import pages.Manager_Page;
import pages.NewCustomer_Page;

public class DeleteCustomer_Demo extends BaseTest {

  public DeleteCustomer_Demo()
  {
    super();
  }

  @Parameters({"browser"})
  @BeforeTest(alwaysRun = true)
  public void beforeTest(String browser) {
    setData("RegisteredUser");
    loginPage.openBrowserAndAccessURL(browser, LOGIN_PAGE_URL);
    loginPage.loginToAccount(findTestData("userId"), findTestData("password"));
    managerPage.clickOnDeleteCustomerLink();
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() {
    webUI.refresh();
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    webUI.refresh();
  }

  @AfterTest(alwaysRun = true)
  public void afterTest() {
    webUI.closeBrowser();
  }

  @Test(groups = {"full_test", "delete"})
  public void DC1_Delete_customer()
  {
    setData("NewCustomerInfo");

    deleteCustomerPage.deleteCustomer(findTestData("customer_id"));
    webUI.navigateTo("https://demo.guru99.com/V4/manager/DeleteCustomerInput.php");
    deleteCustomerPage.deleteCustomer(findTestData("customer_id"));

    assertTrue(deleteCustomerPage.shouldAlertCustomerDoesNotExistBeDisplayed());
  }
}
