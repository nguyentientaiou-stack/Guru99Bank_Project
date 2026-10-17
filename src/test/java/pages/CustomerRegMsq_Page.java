package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerRegMsq_Page extends BasePage {

  public CustomerRegMsq_Page(WebUI webUI) {
    super(webUI);
    setRepo("CustomerRegMsq_Page");
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegMsq_Page.class);

  @Step
  public boolean shouldCustomerNameOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String customerName) {
    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_NAME")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_NAME"), customerName);
  }

  @Step
  public boolean shouldGenderOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String gender) {
    String customerGender = "";
    if (gender.toLowerCase().equals("m") || gender.toLowerCase().equals("male")) {
      customerGender = "male";
    }
    else customerGender = "female";

    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_GENDER")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_GENDER"), customerGender);
  }

  @Step
  public boolean shouldBirthDayOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String birthDate) {
    String reformattedDate = reformatDate(birthDate);
    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_BIRTHDAY")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_BIRTHDAY"), reformattedDate);
  }

  @Step
  public boolean shouldAddressOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String address) {
    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_ADDRESS")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_ADDRESS"), address);
  }

  @Step
  public boolean shouldCityOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String city) {
    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_CITY")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_CITY"), city);
  }

  @Step
  public boolean shouldStateOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String state) {
    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_STATE")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_STATE"), state);
  }

  @Step
  public boolean shouldPINNumberOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String pin) {
    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_PIN")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_PIN"), pin);
  }

  @Step
  public boolean shouldMobileNumberOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String mobileNumber) {
    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_MOBILE_NUMBER")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_MOBILE_NUMBER"), mobileNumber);
  }

  @Step
  public boolean shouldEmailOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(String email) {
    return webUI.verifyElementVisible(findTestObject("DGD_CUSTOMER_EMAIL")) && webUI.verifyElementText(findTestObject("DGD_CUSTOMER_EMAIL"), email);
  }

  @Step
  public boolean shouldCustomerRegisteredHeaderSuccessfullyBeDisplayed(String message)
  {
    if (webUI.verifyElementVisible(findTestObject("LBL_REGISTERED_CUSTOMER_SUCCESSFULLY")) && webUI.verifyElementText(findTestObject("LBL_REGISTERED_CUSTOMER_SUCCESSFULLY"), message))
    {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_REGISTERED_CUSTOMER_SUCCESSFULLY"));
      return true;
    }
    return false;
  }

  @Step
  public String getCustomerIdOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_ID"));
  }

  @Step
  public String getNameOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_NAME"));
  }

  @Step
  public String getGenderOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_GENDER"));
  }

  @Step
  public String getBirthDayOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_BIRTHDAY"));
  }

  @Step
  public String getAddressOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_ADDRESS"));
  }

  @Step
  public String getCityOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_CITY"));
  }

  @Step
  public String getStateOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_STATE"));
  }

  @Step
  public String getPinOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_PIN"));
  }

  @Step
  public String getMobileNumberOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_MOBILE_NUMBER"));
  }

  @Step
  public String getEmailOfNewRegisteredCustomer() {
    return webUI.getText(findTestObject("DGD_CUSTOMER_EMAIL"));
  }
}
