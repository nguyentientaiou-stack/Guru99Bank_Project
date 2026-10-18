package cucumber.steps;

import static org.testng.Assert.assertTrue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginStepdefs extends BaseStepdefs {
  @Given("I open browser and navigate to Guru99Bank Login Page")
  public void openBrowserAndNavigateToGuruBankLoginPage() {
    webUI.openBrowser("Chrome");
    webUI.navigateTo("https://demo.guru99.com/V4/index.php");
  }

  @When("I click on UserID field and do not input any data to the field and press Tab to move to next field")
  public void clickOnUserIDFieldAndDoNotInputAnyDataToTheField() {
    loginPage.inputUserID("");
  }

  @Then("I should see an error message {string} for the UserID field")
  public void shouldSeeAnErrorMessageForTheUserIDField(String errorMessage) {
    assertTrue(loginPage.shouldErrorMessageShowForUserIDField(errorMessage));
  }

  @When("I click on Password field and do not input any data to the field and press Tab to move to next field")
  public void clickOnPasswordFieldAndDoNotInputAnyDataToTheFieldAndPressTabToMoveToNextField() {
    loginPage.inputPassword("");
  }

  @Then("I should see an error message {string} for the Password field")
  public void shouldSeeAnErrorMessageForThePasswordField(String errorMessage) {
    loginPage.shouldErrorMessageShowForPasswordField(errorMessage);
  }

  @When("I input UserID field with {string} and Password field with {string}")
  public void inputUserIDFieldWithAndPasswordFieldWith(String userName, String password) {
    loginPage.inputUserID(userName);
    loginPage.inputPassword(password);
  }

  @And("Click on Login button")
  public void clickOnSubmit() {
    loginPage.clickBTNLogin();
  }

  @Then("User is redirected to Manager Page, Manger Id message will be displayed on the page")
  public void userIsRedirectedToManagerPageMangerIdMessageWillBeDisplayedOnPage() {
    assertTrue(loginPage.isMoveToNextPage("https://demo.guru99.com/V4/manager/Managerhomepage.php"));
    assertTrue(mangerPage.shouldMangerIDShowInThePage());
  }
}
