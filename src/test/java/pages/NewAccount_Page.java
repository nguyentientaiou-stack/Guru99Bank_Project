package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;

public class NewAccount_Page extends BasePage{

  public NewAccount_Page(WebUI webUI)
  {
    super(webUI);
    setRepo("NewAccount_Page");
  }
  @Step("Delay page for {0} seconds")
  public void delayPage(int seconds) {
    webUI.delayInSeconds(seconds);
  }

  @Step("Refresh Page")
  public void refreshPage()
  {
    webUI.refresh();
  }

  @Step("Close Browser")
  public void closeBrowser()
  {
    webUI.closeBrowser();
  }

  @Step("Input {0} for Customer ID field")
  public void inputCustomerID(String customerId)
  {
    webUI.inputText(findTestObject("TXT_CUSTOMER_ID"), customerId);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_ID"));
  }

  @Step("Input {0} for Initial Deposit field")
  public void inputInitialDeposit(String initialDeposit)
  {
    webUI.inputText(findTestObject("TXT_INITIAL_DEPOSIT"), initialDeposit);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_INITIAL_DEPOSIT"));
  }

  @Step("Select {0} for Account Type dropdown list")
  public void selectAccountType(String accountType)
  {
    webUI.selectOptionByText(findTestObject("DDL_ACCOUNT_TYPE"), accountType);
    webUI.takeScreenshotWithHighlight(findTestObject("DDL_ACCOUNT_TYPE"));
  }

  @Step("Should the option of account type be selected as {0} as expected")
  public boolean shouldAccountTypeIsSelectedAsExpected(String expectedAccountType)
  {
    webUI.takeScreenshotWithHighlight(findTestObject("DDL_ACCOUNT_TYPE"));
    return webUI.verifyOptionSelectedByText(findTestObject("DDL_ACCOUNT_TYPE"), expectedAccountType);
  }

  @Step("Error message {0} should be shown for Customer ID field")
  public boolean shouldShowErrorMessageForCustomerIDField(String expectedErrorMessage)
  {
    if (webUI.verifyElementVisible(findTestObject("LBL_CUSTOMER_ID_ERROR_MESSAGE")) && webUI.verifyElementText(findTestObject("LBL_CUSTOMER_ID_ERROR_MESSAGE"), expectedErrorMessage))
    {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_CUSTOMER_ID_ERROR_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message {0} should be shown for Initial Deposit field")
  public boolean shouldShowErrorMessageForInitialDepositField(String expectedErrorMessage)
  {
    if (webUI.verifyElementVisible(findTestObject("LBL_INITIAL_DEPOSIT_ERROR_MESSAGE")) && webUI.verifyElementText(findTestObject("LBL_INITIAL_DEPOSIT_ERROR_MESSAGE"), expectedErrorMessage))
    {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_INITIAL_DEPOSIT_ERROR_MESSAGE"));
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for Customer ID field")
  public boolean shouldNotShowErrorMessageForCustomerIDField()
  {
    if(webUI.verifyElementInvisible(findTestObject("LBL_CUSTOMER_ID_ERROR_MESSAGE")))
    {
      webUI.takeScreenshot();
      return true;
    }
    return false;
  }

  @Step("Error message should not be shown for Initial Deposit field")
  public boolean shouldNotShowErrorMessageForInitialDepositField()
  {
    if(webUI.verifyElementInvisible(findTestObject("LBL_INITIAL_DEPOSIT_ERROR_MESSAGE")))
    {
      webUI.takeScreenshot();
      return true;
    }
    return false;
  }

  @Step("Click on New Account Link")
  public void clickNewAccountLink()
  {
    webUI.takeScreenshotWithHighlight(findTestObject("LNK_NEW_ACCOUNT"));
    webUI.click(findTestObject("LNK_NEW_ACCOUNT"));
  }

  @Step("Press Tab at Customer ID field to move to next field")
  public void pressTabAtCustomerIDFieldToMoveToNextField()
  {
    webUI.pressTabToElement(findTestObject("TXT_CUSTOMER_ID"));
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_CUSTOMER_ID"));
  }

  @Step("Add new account with information {0}, {1}, {2}")
  public void addNewAccount(String customerID, String accountType, String initialDeposit)
  {
    inputCustomerID(customerID);
    selectAccountType(accountType);
    inputInitialDeposit(initialDeposit);
  }

  @Step("Access New Account Page URL")
  public void accessNewAccountPage()
  {
    webUI.navigateTo(findTestObject("NEW_ACCOUNT_PAGE_URL"));
  }

  @Step("Should be moved to Insert Account Page")
  public boolean shouldBeMovedToInsertAccountPage()
  {
    if (webUI.getCurrentUrl().equals(findTestObject("INSERT_ACCOUNT_PAGE_URL")))
    {
      webUI.takeScreenshot();
      return true;
    }
    return false;
  }
}
