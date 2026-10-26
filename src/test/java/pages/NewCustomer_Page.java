package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;
import model.Customer;

public class NewCustomer_Page extends BasePage {

  public NewCustomer_Page(WebUI webUI) {
    super(webUI);
    setRepo(NewCustomer_Page.class.getSimpleName());
  }

  @Step("Refresh Page")
  public void refreshPage() {
    webUI.refresh();
  }

  @Step("Close Browser")
  public void closeBrowser() {
    webUI.closeBrowser();
  }

  @Step("Input {0} for Customer ID field")
  public void inputCustomerNameField(String name) {
    if (name.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_CUSTOMER_NAME"));
      webUI.takeScreenshot();
    }
    else
    {
      webUI.inputText(findTestObject("TXT_CUSTOMER_NAME"), name);
      webUI.pressTabToElement(findTestObject("TXT_CUSTOMER_NAME"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_NAME"));
    }
  }

  @Step("Input {0} for City field")
  public void inputCityField(String city) {
    if (city.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_CITY"));
      webUI.takeScreenshot();
    }
    else
    {
      webUI.inputText(findTestObject("TXT_CITY"), city);
      webUI.pressTabToElement(findTestObject("TXT_CITY"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_CITY"));
    }
  }

  @Step("Input {0} for State field")
  public void inputStateField(String state) {
    if (state.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_STATE"));
      webUI.takeScreenshot();
    }
    else
    {
      webUI.inputText(findTestObject("TXT_STATE"), state);
      webUI.pressTabToElement(findTestObject("TXT_STATE"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_STATE"));
    }
  }

  @Step("Input {0} for Address field")
  public void inputAddressField(String address) {
    if (address.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXA_ADDRESS"));
      webUI.takeScreenshot();
    }
    else
    {
      webUI.inputText(findTestObject("TXA_ADDRESS"), address);
      webUI.pressTabToElement(findTestObject("TXA_ADDRESS"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXA_ADDRESS"));
    }
  }

  @Step("Input {0} for PIN field")
  public void inputPINField(String pinNumber) {
    if (pinNumber.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_PIN"));
      webUI.takeScreenshot();
    }
    else
    {
      webUI.inputText(findTestObject("TXT_PIN"), pinNumber);
      webUI.pressTabToElement(findTestObject("TXT_PIN"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_PIN"));
    }
  }

  @Step("Input {0} for Telephone field")
  public void inputMobileNumberField(String mobileNumber) {
    if (mobileNumber.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_MOBILE_NUMBER"));
      webUI.takeScreenshot();
    }
    else {
      webUI.inputText(findTestObject("TXT_MOBILE_NUMBER"), mobileNumber);
      webUI.pressTabToElement(findTestObject("TXT_MOBILE_NUMBER"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_MOBILE_NUMBER"));
    }
  }

  @Step("Input {0} for Email field")
  public void inputEmailField(String email) {
    if (email.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_EMAIL"));
      webUI.takeScreenshot();
    }
    else
    {
      webUI.inputText(findTestObject("TXT_EMAIL"), email);
      webUI.pressTabToElement(findTestObject("TXT_EMAIL"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_EMAIL"));
    }
  }

  @Step("Input {0} for Password field")
  public void inputPasswordField(String password) {
    if (password.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_PASSWORD"));
      webUI.takeScreenshot();
    }
    else
    {
      webUI.inputText(findTestObject("TXT_PASSWORD"), password);
      webUI.pressTabToElement(findTestObject("TXT_PASSWORD"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_PASSWORD"));
    }
  }

  @Step("Error message should not be shown for Customer Name field")
  public boolean shouldNotShowErrorMessageForCustomerNameField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_CUSTOMER_NAME_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_CUSTOMER_NAME_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for Address field")
  public boolean shouldNotShowErrorMessageForAddressNameField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_ADDRESS_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_ADDRESS_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for City field")
  public boolean shouldNotShowErrorMessageForCityField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_CITY_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_CITY_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for State field")
  public boolean shouldNotShowErrorMessageForStateField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_STATE_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_STATE_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for Date of Birth field")
  public boolean shouldNotShowErrorMessageForDOBField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_DOB_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_DOB_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for PIN field")
  public boolean shouldNotShowErrorMessageForPINField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_PIN_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_PIN_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for Telephone field")
  public boolean shouldNotShowErrorMessageForMobileNumberField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_MOBILE_NUMBER_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_MOBILE_NUMBER_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for Email field")
  public boolean shouldNotShowErrorMessageForEmailField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_EMAIL_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_EMAIL_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should not be shown for Password field")
  public boolean shouldNotShowErrorMessageForPasswordField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_PASSWORD_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_PASSWORD_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for Customer Name field")
  public boolean shouldShowErrorMessageForCustomerNameField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_CUSTOMER_NAME_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_CUSTOMER_NAME_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_CUSTOMER_NAME_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_CUSTOMER_NAME_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for Date of Birth field")
  public boolean shouldShowErrorMessageForDateOfBirthField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_DOB_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_DOB_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_DOB_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_DOB_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for Address field")
  public boolean shouldShowErrorMessageForAddressNameField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_ADDRESS_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_ADDRESS_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_ADDRESS_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_ADDRESS_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for City field")
  public boolean shouldShowErrorMessageForCityField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_CITY_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_CITY_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_CITY_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_CITY_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for State field")
  public boolean shouldShowErrorMessageForStateField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_STATE_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_STATE_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_STATE_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_STATE_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for PIN field")
  public boolean shouldShowErrorMessageForPINField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_PIN_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_PIN_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_PIN_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_PIN_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for Telephone Number field")
  public boolean shouldShowErrorMessageForMobileNumberField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_MOBILE_NUMBER_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_MOBILE_NUMBER_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_MOBILE_NUMBER_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_MOBILE_NUMBER_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for Email field")
  public boolean shouldShowErrorMessageForEmailField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_EMAIL_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_EMAIL_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_EMAIL_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_EMAIL_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for Password field")
  public boolean shouldShowErrorMessageForPasswordField(String expectedMessage) {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_PASSWORD_MESSAGE"));
    boolean isMessageVisible = webUI.verifyElementVisible(findTestObject("LBL_PASSWORD_MESSAGE"));
    boolean isMessageCorrect = webUI.verifyElementText(findTestObject("LBL_PASSWORD_MESSAGE"), expectedMessage);
    if (isMessageVisible && isMessageCorrect) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_PASSWORD_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Select gender as {0}")
  public void selectGender(String gender) {
    if (gender.toLowerCase().equals("male") || gender.toLowerCase().equals("m")) {
      webUI.click(findTestObject("RDO_GENDER_MALE"));
      webUI.takeScreenshotWithHighlight(findTestObject("RDO_GENDER_MALE"));
      webUI.pressTabToElement(findTestObject("RDO_GENDER_MALE"));
    }
    else {
      webUI.click(findTestObject("RDO_GENDER_FEMALE"));
      webUI.takeScreenshotWithHighlight(findTestObject("RDO_GENDER_FEMALE"));
      webUI.pressTabToElement(findTestObject("RDO_GENDER_FEMALE"));
    }
  }

  @Step("Set DOB as {0}")
  public void setDateOfBirth(String dateOfBirth) {
    if (dateOfBirth.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("DTP_DATE_OF_BIRTH"));
      webUI.takeScreenshot();
    }
    else
    {
      webUI.setDateForDTPWithInputTagByJS(findTestObject("DTP_DATE_OF_BIRTH"), dateOfBirth);
      webUI.pressTabToElement(findTestObject("DTP_DATE_OF_BIRTH"));
      webUI.takeScreenshotWithHighlight(findTestObject("DTP_DATE_OF_BIRTH"));
    }
  }

  @Step("Click Submit Button")
  public void clickSubmit() {
    webUI.takeScreenshotWithHighlight(findTestObject("SUBMIT_BUTTON"));
    webUI.click(findTestObject("BTN_SUBMIT"));
  }

  @Step("Input all customer information fields")
  public void inputAllCustomerInformationFields(Customer customer) {
    inputCustomerNameField(customer.getCustomerName());
    selectGender(customer.getGender());
    setDateOfBirth(customer.getDob());
    inputAddressField(customer.getAddress());
    inputCityField(customer.getCity());
    inputStateField(customer.getState());
    inputPINField(customer.getPin());
    inputMobileNumberField(customer.getTelephone());
    inputEmailField(customer.getEmail());
    inputPasswordField(customer.getPassword());
  }

  @Step("Add a new customer")
  public void addNewCustomer(Customer customer) {
    inputAllCustomerInformationFields(customer);
    webUI.click(findTestObject("BTN_SUBMIT"));
  }

  @Step("Should be moved to page {0}")
  public boolean IsMovedToPage(String url) {
    webUI.delayInSeconds(6);
    if (webUI.getCurrentUrl().contains(url))
    {
      webUI.takeScreenshot();
      return true;
    }
    return false;
  }
}
