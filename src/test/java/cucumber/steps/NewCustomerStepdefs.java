package cucumber.steps;

import static org.testng.AssertJUnit.assertTrue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.Customer;

public class NewCustomerStepdefs extends BaseStepdefs {
  @Given("I login to Guru99Bank Page and access New Customer function from the left menu")
  public void loginToGuruBankPageAndAccessNewCustomerFunctionFromTheLeftMenu() {
    webUI.openBrowser("Chrome");
    webUI.navigateTo("https://demo.guru99.com/V4/");
    loginPage.loginToAccount(userName, password);
    mangerPage.clickOnNewCustomerLink();
  }

  @When("I input data to Customer Name field with {string}")
  public void inputDataToCustomerNameFieldWith(String customerName) {
    if (customerName.contains("<SPACE>"))
    {
      customerName = customerName.replace("<SPACE>", " ");
    }
    newCustomerPage.inputCustomerNameField(customerName);
  }

  @Then("I should see an error message {string} for the Customer Name field")
  public void shouldSeeAnErrorMessageForTheCustomerNameField(String errorMessage) {
    assertTrue(newCustomerPage.shouldShowErrorMessageForCustomerNameField(errorMessage));
  }

  @When("I input data to Address field with {string}")
  public void inputDataToAddressFieldWith(String address) {
    if (address.contains("<SPACE>"))
    {
      address = address.replace("<SPACE>", " ");
    }
    newCustomerPage.inputAddressField(address);
  }

  @Then("I should see an error message {string} for the Address field")
  public void shouldSeeAnErrorMessageForTheAddressField(String errorMessage) {
    assertTrue(newCustomerPage.shouldShowErrorMessageForAddressNameField(errorMessage));
  }

  @When("I input data to City field with {string}")
  public void inputDataToCityFieldWith(String city) {
    if (city.contains("<SPACE>"))
    {
      city = city.replace("<SPACE>", " ");
    }
    newCustomerPage.inputCityField(city);
  }

  @Then("I should see an error message {string} for the City field")
  public void shouldSeeAnErrorMessageForTheCityField(String city) {
    assertTrue(newCustomerPage.shouldShowErrorMessageForCityField(city));
  }

  @When("I input data to State field with {string}")
  public void inputDataToStateFieldWith(String state) {
    newCustomerPage.inputStateField(state);
  }

  @Then("I should see an error message {string} for the State field")
  public void shouldSeeAnErrorMessageForTheStateField(String state) {
    assertTrue(newCustomerPage.shouldShowErrorMessageForStateField(state));
  }

  @When("I input data to PIN field with {string}")
  public void inputDataToPINFieldWith(String pin) {
    newCustomerPage.inputPINField(pin);
  }

  @Then("I should see an error message {string} for the PIN field")
  public void shouldSeeAnErrorMessageForThePINField(String errorMessage) {
    assertTrue(newCustomerPage.shouldShowErrorMessageForPINField(errorMessage));
  }

  @When("I input data to Mobile Number field with {string}")
  public void inputDataToMobileNumberFieldWith(String mobile) {
    if (mobile.contains("<SPACE>"))
    {
      mobile = mobile.replace("<SPACE>", " ");
    }
    newCustomerPage.inputMobileNumberField(mobile);
  }

  @Then("I should see an error message {string} for the Mobile Number field")
  public void shouldSeeAnErrorMessageForTheMobileNumberField(String errorMessage) {
    assertTrue(newCustomerPage.shouldShowErrorMessageForMobileNumberField(errorMessage));
  }

  @When("I input data to Email field with {string}")
  public void inputDataToEmailFieldWith(String email) {
    newCustomerPage.inputEmailField(email);
  }

  @Then("I should see an error message {string} for the Email field")
  public void shouldSeeAnErrorMessageForTheEmailField(String errorMessage) {
    assertTrue(newCustomerPage.shouldShowErrorMessageForEmailField(errorMessage));
  }

  @When("I input data to Password field with {string}")
  public void inputDataToPasswordFieldWith(String password) {
    newCustomerPage.inputPasswordField(password);
  }

  @Then("I should see an error message {string} for the Password field of New Customer form")
  public void shouldSeeAnErrorMessageForThePasswordField(String errorMessage) {
    assertTrue(newCustomerPage.shouldShowErrorMessageForPasswordField(errorMessage));
  }

  @And("I click Submit Button")
  public void clickSubmitButton() {
    newCustomerPage.clickSubmit();
  }

  @Then("I should be transferred to Customer Registered Message page and on the page the message {string} is displayed")
  public void shouldBeTransferredToCustomerRegisteredMessagePageAndOnThePageTheMessageIsDisplayed(String message) {
    assertTrue(newCustomerPage.IsMovedToPage("https://demo.guru99.com/V4/manager/CustomerRegMsg.php"));
    assertTrue(customerRegMsqPage.shouldCustomerRegisteredHeaderSuccessfullyBeDisplayed(message));
  }

  @When("I input new customer with information {string}, {string}, {string}, {string} , {string}, {string}, {string}, {string}, {string}, {string}")
  public void inputNewCustomerInformation(String customerName, String gender, String dob, String address, String city, String state, String PIN, String mobile, String email, String password) {
    Customer customer = new Customer(customerName, gender, dob, address, city, state, PIN, mobile, email, password);
    newCustomerPage.inputAllCustomerInformationFields(customer);
  }

  @And("Information of new customer: {string}, {string}, {string}, {string} , {string}, {string}, {string}, {string}, {string} should be displayed on table Customer Registered Successfully")
  public void shouldInformationOfNewCustomerBeDisplayedOnTableCustomerRegisteredSuccessfully(String customerName, String gender, String dob, String address, String city, String state, String PIN, String mobile, String email) {
    assertTrue(customerRegMsqPage.shouldCustomerNameOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(customerName));
    assertTrue(customerRegMsqPage.shouldGenderOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(gender));
    assertTrue(customerRegMsqPage.shouldBirthDayOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(dob));
    assertTrue(customerRegMsqPage.shouldAddressOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(address));
    assertTrue(customerRegMsqPage.shouldCityOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(city));
    assertTrue(customerRegMsqPage.shouldStateOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(state));
    assertTrue(customerRegMsqPage.shouldPINNumberOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(PIN));
    assertTrue(customerRegMsqPage.shouldMobileNumberOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(mobile));
    assertTrue(customerRegMsqPage.shouldEmailOfNewUserBeDisplayedInRegisteredCustomerDetailsTable(email));
  }
}
