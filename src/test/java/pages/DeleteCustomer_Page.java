package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;

public class DeleteCustomer_Page extends BasePage {
  public DeleteCustomer_Page(WebUI webUI) {
    super(webUI);
    setRepo("DeleteCustomer_Page");
  }

  @Step("Refresh page")
  public void refreshPage()
  {
    webUI.refresh();
  }

  @Step("Input {0} to the Customer ID field")
  public void inputCustomerID(String customerID){
    if(customerID.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_CUSTOMER_ID"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_ID"));
    }
    webUI.inputText(findTestObject("TXT_CUSTOMER_ID"), customerID);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_ID"));
  }

  @Step("Error message {0} should be shown for Customer ID field")
  public boolean shouldShowErrorMessageForCustomerIDField(String expectedMessage){
    if (webUI.verifyElementVisible(findTestObject("LBL_CUSTOMER_ID_MESSAGE"))) {
      webUI.verifyElementText(findTestObject("LBL_CUSTOMER_ID_MESSAGE"), expectedMessage);
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_CUSTOMER_ID_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Click Submit button")
  public void clickSubmitButton(){
    webUI.takeScreenshotWithHighlight(findTestObject("BTN_SUBMIT"));
    webUI.click(findTestObject("BTN_SUBMIT"));
  }

  @Step("Delete customer {0}")
  public void deleteCustomer(String customerID){
    inputCustomerID(customerID);
    clickSubmitButton();
    webUI.acceptAlert();
  }

  @Step("Alert customer does not exist should be displayed")
  public boolean shouldAlertCustomerDoesNotExistBeDisplayed() {
    webUI.delayInSeconds(4);
    String alertMessage = webUI.getAlertTextThenAcceptAlert();
    return alertMessage.trim().equals("Customer does not exist!!");
  }
}
