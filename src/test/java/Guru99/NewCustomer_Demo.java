package Guru99;

import static org.testng.AssertJUnit.assertTrue;

import common.keywords.WebUI;
import model.Customer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class NewCustomer_Demo extends BaseTest {

  public NewCustomer_Demo() {
    super();
  }

  @Parameters({"browser"})
  @BeforeTest(alwaysRun = true)
  public void beforeTest(String browser) {
    setData("RegisteredUser");

    loginPage.openBrowserAndAccessURL(browser, LOGIN_PAGE_URL);
    loginPage.loginToAccount(findTestData("userId"), findTestData("password"));
    managerPage.clickOnNewCustomerLink();
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() {
    setData("NewCustomer");

    webUI.refresh();
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() { webUI.refresh();}

  @AfterTest(alwaysRun = true)
  public void afterTest() {
    webUI.closeBrowser();
  }

  @Test(groups = {"full_test"}, priority = 1)
  public void NC1_Name_can_not_be_empty() {
    newCustomerPage.inputCustomerNameField(findTestData("NC1.customerName_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForCustomerNameField(findTestData("NC1.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 2)
  public void NC2_Name_can_not_be_numberic() {
    newCustomerPage.inputCustomerNameField(findTestData("NC2.customerName_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForCustomerNameField(findTestData("NC2.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputCustomerNameField(findTestData("NC2.customerName_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForCustomerNameField(findTestData("NC2.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 3)
  public void NC3_Name_can_not_have_special_characters() {
    newCustomerPage.inputCustomerNameField(findTestData("NC3.customerName_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForCustomerNameField(findTestData("NC3.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputCustomerNameField(findTestData("NC3.customerName_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForCustomerNameField(findTestData("NC3.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 4)
  public void NC4_Name_can_not_have_first_character_as_blank_space() {
    newCustomerPage.inputCustomerNameField(findTestData("NC4.customerName_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForCustomerNameField(findTestData("NC4.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 5)
  public void NC5_Address_can_not_be_empty() {
    newCustomerPage.inputAddressField(findTestData("NC5.address_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForAddressNameField(findTestData("NC5.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 6)
  public void NC6_Address_can_not_have_first_blank_space() {
    newCustomerPage.inputAddressField(findTestData("NC6.address_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForAddressNameField(findTestData("NC6.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 7)
  public void NC7_DOB_can_not_be_empty() {
    newCustomerPage.setDateOfBirth("");
    assertTrue(newCustomerPage.shouldShowErrorMessageForDateOfBirthField(findTestData("NC7.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 8)
  public void NC8_City_can_not_be_empty() {
    newCustomerPage.inputCityField(findTestData("NC8.city_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForCityField(findTestData("NC8.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 9)
  public void NC9_City_can_not_be_numeric() {
    newCustomerPage.inputCityField(findTestData("NC9.city_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForCityField(findTestData("NC9.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputCityField(findTestData("NC9.city_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForCityField(findTestData("NC9.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 10)
  public void NC10_City_can_not_have_special_characters() {
    newCustomerPage.inputCityField(findTestData("NC10.city_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForCityField(findTestData("NC10.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputCityField(findTestData("NC10.city_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForCityField(findTestData("NC10.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 11)
  public void NC11_City_can_not_have_first_blank_space() {
    newCustomerPage.inputCityField(findTestData("NC11.city_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForCityField(findTestData("NC11.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 12)
  public void NC12_State_can_not_be_empty() {
    newCustomerPage.inputStateField(findTestData("NC12.state_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForStateField(findTestData("NC12.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 13)
  public void NC13_State_can_not_be_numeric() {
    newCustomerPage.inputStateField(findTestData("NC13.state_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForStateField(findTestData("NC13.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputStateField(findTestData("NC13.state_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForStateField(findTestData("NC13.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 14)
  public void NC14_State_can_not_have_special_character() {
    newCustomerPage.inputStateField(findTestData("NC14.state_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForStateField(findTestData("NC14.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputStateField(findTestData("NC14.state_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForStateField(findTestData("NC14.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 15)
  public void NC15_State_can_not_have_first_blank_space() {
    newCustomerPage.inputStateField(findTestData("NC15.state_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForStateField(findTestData("NC15.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 16)
  public void NC16_PIN_must_be_numeric() {
    newCustomerPage.inputPINField(findTestData("NC16.pin_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForPINField(findTestData("NC16.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 17)
  public void NC17_PIN_must_not_be_blank() {
    newCustomerPage.inputPINField(findTestData("NC17.pin_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForPINField(findTestData("NC17.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 18)
  public void NC18_PIN_must_have_six_digits() {
    newCustomerPage.inputPINField(findTestData("NC18.pin_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForPINField(findTestData("NC18.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 19)
  public void NC19_PIN_can_not_have_special_character() {
    newCustomerPage.inputPINField(findTestData("NC19.pin_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForPINField(findTestData("NC19.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputPINField(findTestData("NC19.pin_test_data_2"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForPINField(findTestData("NC19.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 20)
  public void NC20_PIN_can_not_have_first_blank_space() {
    newCustomerPage.inputPINField(findTestData("NC20.pin_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForPINField(findTestData("NC20.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 21)
  public void NC21_PIN_can_not_have_blank_space() {
    newCustomerPage.inputPINField(findTestData("NC21.pin_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForPINField(findTestData("NC21.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 22)
  public void NC22_Mobile_number_can_not_be_empty() {
    newCustomerPage.inputMobileNumberField(findTestData("NC22.mobile_number_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForMobileNumberField(findTestData("NC22.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 23)
  public void NC23_Mobile_number_can_not_have_first_character_as_blank_space() {
    newCustomerPage.inputMobileNumberField(findTestData("NC23.mobile_number_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForMobileNumberField(findTestData("NC23.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 24)
  public void NC24_Mobile_number_can_not_have_spaces() {
    newCustomerPage.inputMobileNumberField(findTestData("NC24.mobile_number_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForMobileNumberField(findTestData("NC24.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 25)
  public void NC25_Mobile_number_can_not_have_special_character() {
    newCustomerPage.inputMobileNumberField(findTestData("NC25.mobile_number_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForMobileNumberField(findTestData("NC25.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputMobileNumberField(findTestData("NC25.mobile_number_test_data_2"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForMobileNumberField(findTestData("NC25.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputMobileNumberField(findTestData("NC25.mobile_number_test_data_3"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForMobileNumberField(findTestData("NC25.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 26)
  public void NC26_Email_can_not_be_empty() {
    newCustomerPage.inputMobileNumberField(findTestData("NC26.email_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForEmailField(findTestData("NC26.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 27)
  public void NC27_Email_must_be_in_correct_format() {
    newCustomerPage.inputEmailField(findTestData("NC27.email_test_data_1"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForEmailField(findTestData("NC27.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputEmailField(findTestData("NC27.email_test_data_2"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForEmailField(findTestData("NC27.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputEmailField(findTestData("NC27.email_test_data_3"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForEmailField(findTestData("NC27.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputEmailField(findTestData("NC27.email_test_data_4"));
    softAssert.assertTrue(newCustomerPage.shouldShowErrorMessageForEmailField(findTestData("NC27.expected_error_message")));

    newCustomerPage.refreshPage();
    newCustomerPage.inputEmailField(findTestData("NC27.email_test_data_5"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForEmailField(findTestData("NC27.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 29)
  public void NC29_Email_can_not_have_space() {
    newCustomerPage.inputEmailField(findTestData("NC29.email_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForEmailField(findTestData("NC29.expected_error_message")));
  }

  @Test(groups = {"full_test"}, priority = 30)
  public void NC30_Password_can_not_be_blank(){
    newCustomerPage.inputPasswordField(findTestData("NC30.password_test_data_1"));
    assertTrue(newCustomerPage.shouldShowErrorMessageForPasswordField(findTestData("NC30.expected_error_message")));
  }

  @Test(groups = {"full_test", "retest"}, priority = 31)
  public void NC31_Input_valid_format_data_for_all_fields() {
    Customer customer = new Customer(findTestData("NC31.customer_name"), findTestData("NC31.gender"), findTestData("NC31.dob"),
        findTestData("NC31.address"), findTestData("NC31.city"), findTestData("NC31.state"),
        findTestData("NC31.pin_number"), findTestData("NC31.mobile_number"), findTestData("NC31.email"), findTestData("NC31.password"));

    newCustomerPage.inputAllCustomerInformationFields(customer);

    assertTrue(newCustomerPage.shouldNotShowErrorMessageForCustomerNameField());
    assertTrue(newCustomerPage.shouldNotShowErrorMessageForDOBField());
    assertTrue(newCustomerPage.shouldNotShowErrorMessageForAddressNameField());
    assertTrue(newCustomerPage.shouldNotShowErrorMessageForCityField());
    assertTrue(newCustomerPage.shouldNotShowErrorMessageForStateField());
    assertTrue(newCustomerPage.shouldNotShowErrorMessageForPINField());
    assertTrue(newCustomerPage.shouldNotShowErrorMessageForMobileNumberField());
    assertTrue(newCustomerPage.shouldNotShowErrorMessageForEmailField());
    assertTrue(newCustomerPage.shouldNotShowErrorMessageForPasswordField());
  }

  @Test(groups = {"full_test", "create", "retest"}, priority = 32)
  public void NC32_Create_a_new_customer() {
    String customerName = faker.name().firstName();
    String gender = faker.demographic().sex();
    String dob = faker.timeAndDate().birthday("dd-MMM-yyyy");
    String address = faker.address().streetAddress();
    String city = faker.address().city();
    String state = faker.address().state();
    String pin = faker.number().digits(6);
    String mobileNumber = faker.number().digits(9);
    String emailNewUser = faker.internet().emailAddress();
    String password = faker.credentials().password(10, 10);
    Customer customer = new Customer(customerName, gender, dob, address, city, state, pin, mobileNumber, emailNewUser, password);

    newCustomerPage.addNewCustomer(customer);

    assertTrue(newCustomerPage.IsMovedToPage(CUSTOMER_REG_MSG_PAGE_URL));
    assertTrue(customerRegMsqPage.shouldCustomerRegisteredHeaderSuccessfullyBeDisplayed(findTestData("NC32.expected_register_successfully_message")));
    assertTrue(customerRegMsqPage.shouldCustomerNameOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getCustomerName()));
    assertTrue(customerRegMsqPage.shouldGenderOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getGender()));
    assertTrue(customerRegMsqPage.shouldBirthDayOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getDob()));
    assertTrue(customerRegMsqPage.shouldAddressOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getAddress()));
    assertTrue(customerRegMsqPage.shouldCityOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getCity()));
    assertTrue(customerRegMsqPage.shouldStateOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getState()));
    assertTrue(customerRegMsqPage.shouldPINNumberOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getPin()));
    assertTrue(customerRegMsqPage.shouldMobileNumberOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getTelephone()));
    assertTrue(customerRegMsqPage.shouldEmailOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customer.getEmail()));

    getInformationOfNewCustomerFromCustomerRegMegPageToDataFile();
  }

  public void getInformationOfNewCustomerFromCustomerRegMegPageToDataFile()
  {
    setData("NewCustomerInfo");
    fileHelper.writeDataToJsonFile(getData(), "customer_id", customerRegMsqPage.getCustomerIdOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_name", customerRegMsqPage.getNameOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_gender", customerRegMsqPage.getGenderOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_birthday", customerRegMsqPage.getBirthDayOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_address", customerRegMsqPage.getAddressOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_city", customerRegMsqPage.getCityOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_state", customerRegMsqPage.getStateOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_pin", customerRegMsqPage.getPinOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_mobile_number", customerRegMsqPage.getMobileNumberOfNewRegisteredCustomer());
    fileHelper.writeDataToJsonFile(getData(), "customer_email", customerRegMsqPage.getEmailOfNewRegisteredCustomer());
  }
}
