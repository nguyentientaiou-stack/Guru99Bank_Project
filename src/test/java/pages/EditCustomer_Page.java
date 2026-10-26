package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;

public class EditCustomer_Page extends BasePage {

  public EditCustomer_Page(WebUI webUI) {
    super(webUI);
    setRepo("EditCustomer_Page");
  }

  @Step("Refresh Page")
  public void refreshPage() {
    webUI.refresh();
  }

  @Step
  public void inputCustomerIDField(String customerID) {
    if (customerID.isEmpty()) {
      webUI.pressTabToElement(findTestObject("TXT_CUSTOMER_ID"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_ID"));
    } else {
      webUI.inputText(findTestObject("TXT_CUSTOMER_ID"), customerID);
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_ID"));
    }
  }

  @Step
  public void clickSubmit() {
    webUI.takeScreenshotWithHighlight(findTestObject("BTN_SUBMIT"));
    webUI.click(findTestObject("BTN_SUBMIT"));
  }

  @Step
  public boolean shouldShowErrorMessageForCustomerIDField(String expectedMessage) {
    if (webUI.verifyElementVisible(findTestObject("LBL_CUSTOMER_ID_MESSAGE")) && webUI.verifyElementText(findTestObject("LBL_CUSTOMER_ID_MESSAGE"), expectedMessage)) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_CUSTOMER_ID_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step
  public boolean shouldNotShowErrorMessageForCustomerIDField() {
    if (webUI.verifyElementInvisible(findTestObject("LBL_CUSTOMER_ID_MESSAGE"))) {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_CUSTOMER_ID_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Input {0} for Password field")
  public void inputAddressField(String address) {
    webUI.inputText(findTestObject("TXA_ADDRESS"), address);
    webUI.takeScreenshotWithHighlight(findTestObject("TXA_ADDRESS"));
  }

  @Step("Input {0} for City field")
  public void inputCityField(String city) {
    webUI.inputText(findTestObject("TXT_CITY"), city);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_CITY"));
  }

  @Step("Input {0} for State field")
  public void inputStateField(String state) {
    webUI.inputText(findTestObject("TXT_STATE"), state);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_STATE"));
  }

  @Step("Input {0} for PIN field")
  public void inputPINField(String pinNumber) {
    webUI.inputText(findTestObject("TXT_PIN"), pinNumber);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_PIN"));
  }

  @Step("Input {0} for Telephone field")
  public void inputMobileNumberField(String mobileNumber) {
    webUI.inputText(findTestObject("TXT_MOBILE_NUMBER"), mobileNumber);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_MOBILE_NUMBER"));
  }

  @Step("Input {0} for Email field")
  public void inputEmailField(String email) {
    webUI.inputText(findTestObject("TXT_EMAIL"), email);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_EMAIL"));
  }

  @Step("Press Tab at City field to move to next field")
  public void pressTabAtCityFieldToMoveToNextField() {
    webUI.pressTabToElement(findTestObject("TXT_CITY"));
  }

  @Step("Press Tab at State field to move to next field")
  public void pressTabAtDateOfBirthFieldToMoveToNextField() {
    webUI.pressTabToElement(findTestObject("DTP_DATE_OF_BIRTH"));
  }

  @Step("Press Tab at State field to move to next field")
  public void pressTabAtStateFieldToMoveToNextField() {
    webUI.pressTabToElement(findTestObject("TXT_STATE"));
  }

  @Step("Press Tab at Address field to move to next field")
  public void pressTabAtAddressFieldToMoveToNextField() {
    webUI.pressTabToElement(findTestObject("TXA_ADDRESS"));;
  }

  @Step("Press Tab at PIN field to move to next field")
  public void pressTabAtPINFieldToMoveToNextField() {
    webUI.pressTabToElement(findTestObject("TXT_PIN"));
  }

  @Step("Press Tab at Telephone field to move to next field")
  public void pressTabAtMobileNumberFieldToMoveToNextField() {
    webUI.pressTabToElement(findTestObject("TXT_MOBILE_NUMBER"));
  }

  @Step("Press Tab at Email field to move to next field")
  public void pressTabAtEmailFieldToMoveToNextField() {
    webUI.pressTabToElement(findTestObject("TXT_EMAIL"));
  }

  @Step("Update Address of customer")
  public void updateAddressOfCustomer(String address) {
    webUI.clearText(findTestObject("TXA_ADDRESS"));
    inputAddressField(address);
    pressTabAtAddressFieldToMoveToNextField();
  }

  @Step("Update City of customer")
  public void updateCityOfCustomer(String city) {
    webUI.clearText(findTestObject("TXT_CITY"));
    inputCityField(city);
    pressTabAtCityFieldToMoveToNextField();
  }

  @Step("Update State of customer")
  public void updateStateOfCustomer(String state) {
    webUI.clearText(findTestObject("TXT_STATE"));
    inputStateField(state);
    pressTabAtStateFieldToMoveToNextField();
  }

  @Step("Update PIN of customer")
  public void updatePINOfCustomer(String pin) {
    webUI.clearText(findTestObject("TXT_PIN"));
    inputPINField(pin);
    pressTabAtPINFieldToMoveToNextField();
  }

  @Step("Update Mobile Number of customer")
  public void updateMobileNumberOfCustomer(String mobileNumber) {
    webUI.clearText(findTestObject("TXT_MOBILE_NUMBER"));
    inputMobileNumberField(mobileNumber);
    pressTabAtMobileNumberFieldToMoveToNextField();
  }

  @Step("Update Email of customer")
  public void updateEmailOfCustomer(String email) {
    webUI.clearText(findTestObject("TXT_EMAIL"));
    inputEmailField(email);
    pressTabAtEmailFieldToMoveToNextField();
  }

  @Step("Update address as {0}, city {1}, state {2}, pin {3}, mobile {4}, email {5} for customer")
  public void updateInformationOfCustomer(String address, String city, String state, String pin, String mobileNumber, String email) {
    updateAddressOfCustomer(address);
    updateCityOfCustomer(city);
    updateStateOfCustomer(state);
    updatePINOfCustomer(pin);
    updateMobileNumberOfCustomer(mobileNumber);
    updateEmailOfCustomer(email);
    clickSubmit();
  }

  @Step("Customer name should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerNameBeLoadedCorrectlyOnEditCustomerForm(String customerName) {
    String actual_customerName = webUI.getAttributeValue(findTestObject("TXT_CUSTOMER_NAME"), "value");
    if (actual_customerName.equals(customerName)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_NAME"));
      return true;
    }
    return false;
  }

  @Step("Customer gender should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerGenderBeLoadedCorrectlyOnEditCustomerForm(String customerGender) {
    String actual_customerGender = webUI.getAttributeValue(findTestObject("TXT_GENDER"), "value");
    if (actual_customerGender.equals(customerGender)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_GENDER"));
      return true;
    }
    return false;
  }

  @Step("Customer DOB should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerDOBBeLoadedCorrectlyOnEditCustomerForm(String DOB) {
    String actual_customerDOB = webUI.getAttributeValue(findTestObject("TXT_DOB"), "value");
    if (actual_customerDOB.equals(DOB)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_DOB"));
      return true;
    }
    return false;
  }

  @Step("Customer address should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerAddressBeLoadedCorrectlyOnEditCustomerForm(String customerAddress) {
    String actual_customerAddress = webUI.getAttributeValue(findTestObject("TXA_ADDRESS"), "value");
    if (actual_customerAddress.equals(customerAddress)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXA_ADDRESS"));
      return true;
    }
    return false;
  }

  @Step("Customer city should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerCityBeLoadedCorrectlyOnEditCustomerForm(String customerCity) {
    String actual_customerCity = webUI.getAttributeValue(findTestObject("TXT_CITY"), "value");
    if (actual_customerCity.equals(customerCity)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_CITY"));
      return true;
    }
    return false;
  }

  @Step("Customer state should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerStateBeLoadedCorrectlyOnEditCustomerForm(String customerState) {
    String actual_customerState = webUI.getAttributeValue(findTestObject("TXT_STATE"), "value");
    if (actual_customerState.equals(customerState)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_STATE"));
      return true;
    }
    return false;
  }

  @Step("Customer PIN should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerPINBeLoadedCorrectlyOnEditCustomerForm(String customerPIN) {
    String actual_customerPIN = webUI.getAttributeValue(findTestObject("TXT_PIN"), "value");
    if (actual_customerPIN.equals(customerPIN)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_PIN"));
      return true;
    }
    return false;
  }

  @Step("Customer mobile should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerMobileNumberBeLoadedCorrectlyOnEditCustomerForm(String customerMobileNumber) {
    String actual_customerMobile = webUI.getAttributeValue(findTestObject("TXT_MOBILE_NUMBER"), "value");
    if (actual_customerMobile.equals(customerMobileNumber)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_MOBILE_NUMBER"));
      return true;
    }
    return false;
  }

  @Step("Customer email should be loaded correctly on Edit Customer form")
  public boolean shouldCustomerEmailBeLoadedCorrectlyOnEditCustomerForm(String customerEmail) {
    String actual_customerEmail = webUI.getAttributeValue(findTestObject("TXT_EMAIL"), "value");
    if (actual_customerEmail.equals(customerEmail)) {
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_EMAIL"));
      return true;
    }
    return false;
  }
}
