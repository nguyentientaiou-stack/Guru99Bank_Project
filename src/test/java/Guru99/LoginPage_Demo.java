package Guru99;

import static org.testng.Assert.assertTrue;

import common.keywords.WebUI;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.Login_Page;
import pages.Manager_Page;
import utils.FileHelper;

public class LoginPage_Demo extends BaseTest{

  public LoginPage_Demo(){
    super();
  }

  @Parameters({"browser"})
  @BeforeTest(alwaysRun = true)
  public void beforeTest(String browser) {
    setData("Login");
    loginPage.openBrowserAndAccessURL(browser, LOGIN_PAGE_URL);
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() {
    webUI.refresh();
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() { webUI.refresh();}

  @AfterTest(alwaysRun = true)
  public void afterTest() {
    webUI.closeBrowser();
  }

  @Test(groups = {"full_test"}, priority = 1)
  public void LG1_Verify_the_login_page_show_text_for_user_id(){
    loginPage.inputUserID(findTestData("LG1.user_id"));
    assertTrue(loginPage.shouldErrorMessageShowForUserIDField(findTestData("LG1.error_message")));
  }

  @Test(groups = {"full_test"}, priority = 2)
  public void LG2_Verify_the_login_page_show_text_for_password(){
    loginPage.inputPassword(findTestData("LG2.password"));
    assertTrue(loginPage.shouldErrorMessageShowForPasswordField(findTestData("LG2.error_message")));
  }

  @Test(groups = {"full_test"}, priority = 3)
  public void LG3_Verify_the_login_page_show_text_for_name(){
    setData("RegisteredUser");

    loginPage.loginToAccount(findTestData("userId"), findTestData("password"));
    assertTrue(managerPage.shouldMangerIDShowInThePage());
  }
}
