package Guru99;

import static org.testng.Assert.assertTrue;

import common.keywords.WebUI;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.EditCustomer_Page;
import pages.Login_Page;
import pages.Manager_Page;

public class EditCustomer_Demo extends BaseTest {

  public EditCustomer_Demo() {
    super();
  }

  @Parameters({"browser"})
  @BeforeTest(alwaysRun = true)
  public void beforeTest(String browser) {
    setData("RegisteredUser");

    loginPage.openBrowserAndAccessURL(browser, LOGIN_PAGE_URL);
    loginPage.loginToAccount(findTestData("userId"), findTestData("password"));
    managerPage.clickOnEditCustomerLink();
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() {
    setData("EditCustomer");

    editCustomerPage.refreshPage();
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {

    editCustomerPage.refreshPage();
  }

  @AfterTest(alwaysRun = true)
  public void afterTest() {
    webUI.closeBrowser();
  }

  @Test(groups = {"full_test"}, priority = 1)
  public void EC1_Customer_id_can_not_be_empty(){
    editCustomerPage.pressTabAtCustomerIDFieldToMoveToNextField();
    assertTrue(editCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("EC1.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 2)
  public void EC2_Customer_id_must_be_numeric(){
    editCustomerPage.inputCustomerIDField(findTestData("EC2.customerID_test_data_1"));
    softAssert.assertTrue(editCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("EC2.expected_error_message")));

    editCustomerPage.refreshPage();
    editCustomerPage.inputCustomerIDField(findTestData("EC2.customerID_test_data_2"));
    assertTrue(editCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("EC2.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 3)
  public void EC3_Customer_id_can_not_have_special_character(){
    editCustomerPage.inputCustomerIDField(findTestData("EC3.customerID_test_data_1"));
    softAssert.assertTrue(editCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("EC3.expected_error_message")));

    editCustomerPage.refreshPage();
    editCustomerPage.inputCustomerIDField(findTestData("EC3.customerID_test_data_2"));
    assertTrue(editCustomerPage.shouldShowErrorMessageForCustomerIDField(findTestData("EC3.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 4)
  public void EC4_Input_valid_customer_id(){
    setData("NewCustomer");

    editCustomerPage.inputCustomerIDField(findTestData("CUSTOMER_ID"));
    assertTrue(editCustomerPage.shouldNotShowErrorMessageForCustomerIDField());
  }

  @Test(groups = {"full_test", "edit"}, priority = 5)
  public void EC5_Edit_customer_id(){
    setData("NewCustomerInfo");
    editCustomerPage.inputCustomerIDField(findTestData("customer_id"));
    editCustomerPage.clickSubmit();

    assertTrue(editCustomerPage.shouldCustomerNameBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_name")));
    assertTrue(editCustomerPage.shouldCustomerGenderBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_gender")));
    assertTrue(editCustomerPage.shouldCustomerDOBBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_birthday")));
    assertTrue(editCustomerPage.shouldCustomerAddressBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_address")));
    assertTrue(editCustomerPage.shouldCustomerCityBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_city")));
    assertTrue(editCustomerPage.shouldCustomerStateBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_state")));
    assertTrue(editCustomerPage.shouldCustomerPINBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_pin")));
    assertTrue(editCustomerPage.shouldCustomerMobileNumberBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_mobile_number")));
    assertTrue(editCustomerPage.shouldCustomerEmailBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_email")));

    setData("EditCustomer");
    editCustomerPage.updateInformationOfCustomer(findTestData("EC5.address"), findTestData("EC5.city"), findTestData("EC5.state"), findTestData("EC5.pin_number"), findTestData("EC5.mobile_number"), findTestData("EC5.email") );
  }

  @Parameters({"browser"})
  @Test(groups = {"full_test", "edit"}, priority = 6)
  public void EC6_Updated_information_from_EC5_should_be_displayed_On_Edit_Customer_Page(String browser) {
    afterTest();
    beforeTest(browser);
    setData("NewCustomerInfo");

    editCustomerPage.inputCustomerIDField(findTestData("customer_id"));
    editCustomerPage.clickSubmit();

    assertTrue(editCustomerPage.shouldCustomerNameBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_name")));
    assertTrue(editCustomerPage.shouldCustomerGenderBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_gender")));
    assertTrue(editCustomerPage.shouldCustomerDOBBeLoadedCorrectlyOnEditCustomerForm(findTestData("customer_birthday")));
    setData("EditCustomer");
    assertTrue(editCustomerPage.shouldCustomerAddressBeLoadedCorrectlyOnEditCustomerForm(findTestData("EC5.address")));
    assertTrue(editCustomerPage.shouldCustomerCityBeLoadedCorrectlyOnEditCustomerForm(findTestData("EC5.city")));
    assertTrue(editCustomerPage.shouldCustomerStateBeLoadedCorrectlyOnEditCustomerForm(findTestData("EC5.state")));
    assertTrue(editCustomerPage.shouldCustomerPINBeLoadedCorrectlyOnEditCustomerForm(findTestData("EC5.pin_number")));
    assertTrue(editCustomerPage.shouldCustomerMobileNumberBeLoadedCorrectlyOnEditCustomerForm(findTestData("EC5.mobile_number")));
    assertTrue(editCustomerPage.shouldCustomerEmailBeLoadedCorrectlyOnEditCustomerForm(findTestData("EC5.email")));
  }
}
