package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;

public class DeleteCustomer_Page extends BasePage {
  public DeleteCustomer_Page(WebUI webUI) {
    super(webUI);
    setRepo("DeleteCustomer_Page");
  }

  @Step
  public void inputCustomerID(String customerID){
    webUI.inputText(findTestObject("TXT_CUSTOMER_ID"), customerID);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_ID"));
  }

  @Step
  public void clickSubmitButton(){
    webUI.takeScreenshotWithHighlight(findTestObject("BTN_SUBMIT"));
    webUI.click(findTestObject("BTN_SUBMIT"));
  }

  @Step
  public void deleteCustomer(String customerID){
    inputCustomerID(customerID);
    clickSubmitButton();
    webUI.acceptAlert();
  }

  @Step
  public boolean shouldAlertCustomerDoesNotExistBeDisplayed() {
    webUI.delayInSeconds(4);
    String alertMessage = webUI.getAlertTextThenAcceptAlert();
    return alertMessage.trim().equals("Customer does not exist!!");
  }
}
