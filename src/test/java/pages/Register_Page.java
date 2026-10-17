package pages;

import common.keywords.WebUI;
import io.qameta.allure.Step;

public class Register_Page extends BasePage {
  public Register_Page(WebUI webUI) {
    super(webUI);
    setRepo("Register_Page");
  }

  @Step("Input Email ID: {0}")
  private void inputEmailId(String email) {
    webUI.inputText(findTestObject("TXT_EMAIL_ID"), email);
    webUI.takeScreenshotWithHighlight(findTestObject("TXT_EMAIL_ID"));
  }

  @Step("Click Submit button")
  private void clickSubmitButton() {
    webUI.takeScreenshotWithHighlight(findTestObject("BTN_SUBMIT"));
    webUI.click(findTestObject("BTN_SUBMIT"));
  }

  @Step("Register a new user with email: {0}")
  public void register(String email) {
    inputEmailId(email);
    clickSubmitButton();
  }
}
