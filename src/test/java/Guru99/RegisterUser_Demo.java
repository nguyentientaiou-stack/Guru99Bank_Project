package Guru99;

import static org.testng.Assert.assertTrue;

import common.keywords.WebUI;
import org.apache.commons.lang3.StringUtils;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.Login_Page;
import utils.RegisterUserHelper;

public class RegisterUser_Demo extends BaseTest{

  public RegisterUser_Demo()
  {
    super();
  }

  @Parameters({"browser"})
  @BeforeTest(alwaysRun = true)
  public void beforeTest(String browser) {
    loginPage.openBrowserAndAccessURL(browser, LOGIN_PAGE_URL);
  }

  @AfterTest(alwaysRun = true)
  public void afterTest() {
    webUI.closeBrowser();
  }

  @Test(groups = {"full_test"}, description = "Register a new Guru99 user every 20 days and save it to a JSON file")
  public void registerUserWhenExpired() {
    if (!Boolean.getBoolean("forceRegister") && RegisterUserHelper.isValid()) {
      throw new SkipException(
          "Saved user in " + RegisterUserHelper.USER_FILE + " is still valid, no need to register");
    }

    loginPage.registerNewUser();

    assertTrue(RegisterUserHelper.isValid(), "Saved user should be valid after registering");
    assertTrue(StringUtils.startsWith(RegisterUserHelper.getUserId(), "mngr"), "Unexpected User ID: " + RegisterUserHelper.getUserId());
    assertTrue(StringUtils.isNotBlank(RegisterUserHelper.getPassword()), "Password is blank");
  }
}
