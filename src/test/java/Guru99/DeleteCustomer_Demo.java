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

  @Test(groups = {"full_test"})
  public void DC1_Customer_id_can_not_be_empty()
  {
    setData("DeleteCustomer");

    deleteCustomerPage.inputCustomerID(findTestData("DC1.customerName_test_data_1"));

    assertTrue(deleteCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("DC1.expected_error_message")));
  }

  @Test(groups = {"full_test"})
  public void DC2_Customer_id_must_be_numeric()
  {
    setData("DeleteCustomer");

    deleteCustomerPage.inputCustomerID(findTestData("DC2.customerName_test_data_1"));
    assertTrue(deleteCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("DC2.expected_error_message")));
  }

  @Test(groups = {"full_test"})
  public void DC3_Customer_id_can_not_have_special_character()
  {
    setData("DeleteCustomer");

    deleteCustomerPage.inputCustomerID(findTestData("DC3.customerName_test_data_1"));
    assertTrue(deleteCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("DC3.expected_error_message")));

    deleteCustomerPage.refreshPage();
    deleteCustomerPage.inputCustomerID(findTestData("DC3.customerName_test_data_2"));
    assertTrue(deleteCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("DC3.expected_error_message")));
  }

  @Test(groups = {"full_test"})
  public void DC4_Customer_id_can_not_have_blank_space()
  {
    setData("DeleteCustomer");

    deleteCustomerPage.inputCustomerID(findTestData("DC4.customerName_test_data_1"));
    assertTrue(deleteCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("DC4.expected_error_message")));
  }

  @Test(groups = {"full_test"})
  public void DC5_Customer_id_can_not_have_first_character_be_space()
  {
    setData("DeleteCustomer");

    deleteCustomerPage.inputCustomerID(findTestData("DC5.customerName_test_data_1"));
    assertTrue(deleteCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("DC5.expected_error_message")));
  }

  @Parameters({"browser"})
  @Test(groups = {"full_test", "delete"})
  public void DC6_Delete_customer(String browser)
  {
    setData("NewCustomerInfo");

    deleteCustomerPage.deleteCustomer(findTestData("customer_id"));
    webUI.navigateTo(DELETE_CUSTOMER_PAGE);
    deleteCustomerPage.deleteCustomer(findTestData("customer_id"));

    assertTrue(deleteCustomerPage.shouldAlertCustomerDoesNotExistBeDisplayed());
  }
}
