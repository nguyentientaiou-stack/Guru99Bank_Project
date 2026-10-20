Feature: Login Page

  Scenario: Validate UserID field will show error message when the field does not have any inputted data in Login Page
    Given I open browser and navigate to Guru99Bank Login Page
    When  I click on UserID field and do not input any data to the field and press Tab to move to next field
    Then  I should see an error message "User-ID must not be blank" for the UserID field

  Scenario: Validate Password field will show error message when the field does not have any inputted data in Login Page
    Given I open browser and navigate to Guru99Bank Login Page
    When  I click on Password field and do not input any data to the field and press Tab to move to next field
    Then  I should see an error message "Password must not be blank" for the Password field

  Scenario Outline: Validate user can log in successfully
    Given I open browser and navigate to Guru99Bank Login Page
    When  I input UserID field with "<username>" and Password field with "<password>"
    And   Click on Login button
    Then  User is redirected to Manager Page, Manger Id message will be displayed on the page
    Examples:
      | username    | password |
      | mngr668177  | huhUvEr  |