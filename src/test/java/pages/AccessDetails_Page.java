package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;

public class AccessDetails_Page extends BasePage{
  public AccessDetails_Page(WebUI webUI) {
    super(webUI);
    setRepo("AccessDetails_Page");
  }

  @Step("Get User ID")
  public String getUserId() {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_USER_ID"));
    return webUI.getText(findTestObject("LBL_USER_ID")).trim();
  }

  @Step("Get Password")
  public String getPassword() {
    webUI.takeScreenshotWithHighlight(findTestObject("LBL_PASSWORD"));
    return webUI.getText(findTestObject("LBL_PASSWORD")).trim();
  }
}
