package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;

public class Manager_Page extends BasePage {

  public Manager_Page(WebUI webUI)
  {
    super(webUI);
    setRepo("Manager_Page");
  }
  
  @Step("The text Manger ID should be shown in Manager page")
  public boolean shouldMangerIDShowInThePage()
  {
    if (webUI.verifyElementVisible(findTestObject("LBL_MANAGER_ID")))
    {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_MANAGER_ID"));
      return true;
    }
    return false;
  }

  @Step("Access New Customer Page")
  public void clickOnNewCustomerLink()
  {
    webUI.takeScreenshotWithHighlight(findTestObject("LNK_NEW_CUSTOMER"));
    webUI.click(findTestObject("LNK_NEW_CUSTOMER"));
  }

  @Step("Access Edit Customer Page")
  public void clickOnEditCustomerLink()
  {
    webUI.takeScreenshotWithHighlight(findTestObject("LNK_EDIT_CUSTOMER"));
    webUI.click(findTestObject("LNK_EDIT_CUSTOMER"));
  }

  @Step("Access Edit Customer Page")
  public void clickOnDeleteCustomerLink()
  {
    webUI.takeScreenshotWithHighlight(findTestObject("LNK_DELETE_CUSTOMER"));
    webUI.click(findTestObject("LNK_DELETE_CUSTOMER"));
  }
}
