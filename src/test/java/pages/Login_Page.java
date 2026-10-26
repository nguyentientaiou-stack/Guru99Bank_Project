package pages;

import Guru99.BaseTest;
import common.keywords.WebUI;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.devtools.latest.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RegisterUserHelper;

public class Login_Page extends BasePage{

  public Login_Page(WebUI webUI)
  {
    super(webUI);
    setRepo("Login_Page");
  }

  @Step
  public void refreshPage()
  {
    webUI.refresh();
  }

  @Step("Input {0} to User ID field")
  public void inputUserID(String userID)
  {
    if (userID.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_USERID"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_USERID"));
    }
    else
    {
      webUI.inputText(findTestObject("TXT_USERID"), userID);
      webUI.pressTabToElement(findTestObject("TXT_USERID"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_USERID"));
    }
  }

  @Step("Input {0} to Password field")
  public void inputPassword(String password)
  {
    if (password.isEmpty())
    {
      webUI.pressTabToElement(findTestObject("TXT_PASSWORD"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_PASSWORD"));
    }
    else
    {
      webUI.inputText(findTestObject("TXT_PASSWORD"), password);
      webUI.pressTabToElement(findTestObject("TXT_PASSWORD"));
      webUI.takeScreenshotWithHighlight(findTestObject("TXT_PASSWORD"));
    }
  }

  @Step("Click Login Button")
  public void clickBTNLogin()
  {
    webUI.takeScreenshotWithHighlight(findTestObject("BTN_LOGIN"));
    webUI.click(findTestObject("BTN_LOGIN"));
  }

  @Step("Login account with {0} and {1}")
  public void loginToAccount(String userID, String password)
  {
    inputUserID(userID);
    inputPassword(password);
    clickBTNLogin();
  }

  @Step("Error message {0} should be shown for UserID field")
  public boolean shouldErrorMessageShowForUserIDField(String expectedMessage)
  {
   if (webUI.verifyElementVisible(findTestObject("LBL_USERID_MESSAGE")) && webUI.verifyElementText(findTestObject("LBL_USERID_MESSAGE"),  expectedMessage))
   {
     webUI.takeScreenshotWithHighlight(findTestObject("LBL_USERID_MESSAGE"));
     return true;
   }
   webUI.takeScreenshot();
   return false;
  }

  @Step("Error message {0} should be shown for Password field")
  public boolean shouldErrorMessageShowForPasswordField(String expectedMessage)
  {
    if (webUI.verifyElementVisible(findTestObject("LBL_PASSWORD_MESSAGE")) && webUI.verifyElementText(findTestObject("LBL_PASSWORD_MESSAGE"),  expectedMessage))
    {
      webUI.takeScreenshotWithHighlight(findTestObject("LBL_PASSWORD_MESSAGE"));
      return true;
    }
    webUI.takeScreenshot();
    return false;
  }

  @Step("Open browser {0} and access URL {1}")
  public void openBrowserAndAccessURL(String browser, String url) {
    webUI.openBrowser(browser);
    webUI.maximizeWindow();
    webUI.navigateTo(url);
  }

  @Step("The current page is moved to next page {0}")
  public boolean isMoveToNextPage(String url)
  {
    return webUI.getCurrentUrl().equals(url);
  }

  @Step("Click 'here' link at Visit label")
  public void clickVisitHereLink() {
    webUI.takeScreenshotWithHighlight(findTestObject("LNK_VISIT_HERE"));
    webUI.click(findTestObject("LNK_VISIT_HERE"));
  }

  @Step("Register a new user and save it to RegisteredUser.json")
  public void registerNewUser() {
    Register_Page registerPage = new Register_Page(webUI);
    AccessDetails_Page accessDetailsPage = new AccessDetails_Page(webUI);

    String email = "autotest" + System.currentTimeMillis() + "@gmail.com";
    clickVisitHereLink();
    registerPage.register(email);
    String userId = accessDetailsPage.getUserId();
    String password = accessDetailsPage.getPassword();

    if (!StringUtils.startsWith(userId, "mngr") || StringUtils.isBlank(password)) {
      throw new IllegalStateException(
          "Failed to register a new user, got User ID '" + userId + "' and Password '" + password + "'");
    }
    RegisterUserHelper.save(userId, password, email);
  }

  @Step("Invalid username or password alert should appear")
  public boolean shouldInvalidUserNamePasswordAlertAppear(String expectedMessage) {
    return webUI.verifyAlertPresent() && webUI.getAlertTextThenAcceptAlert().equals(expectedMessage);
  }
}

