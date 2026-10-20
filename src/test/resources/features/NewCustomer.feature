Feature: New Customer

  Scenario Outline: Validate Customer Name field will show error message when the field does not have any inputted data or the field has invalid data
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input data to Customer Name field with "<customerName>"
    Then  I should see an error message "<errorMessage>" for the Customer Name field
    Examples:
      | customerName | errorMessage                       |
      |              | Customer name must not be blank    |
      | 1234         | Numbers are not allowed            |
      | name123      | Numbers are not allowed            |
      | name!@#      | Special characters are not allowed |
      | !@#          | Special characters are not allowed |
      | <SPACE>John  | First character can not have space |

  Scenario Outline: Validate Address field will show error message when the field does not have any inputted data or the field has invalid data
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input data to Address field with "<address>"
    Then  I should see an error message "<errorMessage>" for the Address field
    Examples:
      | address               | errorMessage                       |
      |                       | Address Field must not be blank    |
      | <SPACE>TestingAddress | First character can not have space |

  Scenario Outline: Validate City field will show error message when the field does not have any inputted data or the field has invalid data
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input data to City field with "<cityName>"
    Then  I should see an error message "<errorMessage>" for the City field
    Examples:
      | cityName           | errorMessage                       |
      |                    | City Field must not be blank       |
      | 1234               | Numbers are not allowed            |
      | city123            | Numbers are not allowed            |
      | City!@#            | Special characters are not allowed |
      | !@#                | Special characters are not allowed |
      | <SPACE>TestingCity | First character can not have space |

  Scenario Outline: Validate State field will show error message when the field does not have any inputted data or the field has invalid data
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input data to State field with "<stateName>"
    Then  I should see an error message "<errorMessage>" for the State field
    Examples:
      | stateName | errorMessage                       |
      |           | State must not be blank            |
      | 1234      | Numbers are not allowed            |
      | State1234 | Numbers are not allowed            |
      | State!@#  | Special characters are not allowed |
      | !@#       | Special characters are not allowed |

  Scenario Outline: Validate PIN field will show error message when the field does not have any inputted data or the field has invalid data
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input data to PIN field with "<pinNumber>"
    Then  I should see an error message "<errorMessage>" for the PIN field
    Examples:
      | pinNumber | errorMessage                       |
      |           | PIN Code must not be blank         |
      | 1234      | Characters are not allowed         |
      | 123PIN    | Characters are not allowed         |
      | 12        | PIN Code must have 6 Digits        |
      | 123       | PIN Code must have 6 Digits        |
      | 123!@#    | Special characters are not allowed |
      | !@#       | Special characters are not allowed |

  Scenario Outline: Validate Mobile Number field will show error message when the field does not have any inputted data or the field has invalid data
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input data to Mobile Number field with "<mobileNumber>"
    Then  I should see an error message "<errorMessage>" for the Mobile Number field
    Examples:
      | mobileNumber  | errorMessage                       |
      |               | Mobile must not be blank           |
      | <SPACE>123456 | First character can not have space |
      | 123 123       | Characters are not allowed         |
      | 886636!@12    | Special characters are not allowed |
      | !@88662682    | Special characters are not allowed |
      | 88663682!@    | Special characters are not allowed |

  Scenario Outline: Validate Email field will show error message when the field does not have any inputted data or the field has invalid data
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input data to Email field with "<email>"
    Then  I should see an error message "<errorMessage>" for the Email field
    Examples:
      | email             | errorMessage               |
      |                   | Email-ID must not be blank |
      | guru99@gmail      | Email-ID is not valid      |
      | guru99            | Email-ID is not valid      |
      | Guru99@           | Email-ID is not valid      |
      | guru99@gmail.     | Email-ID is not valid      |
      | guru99gmail.com   | Email-ID is not valid      |
      | guru 99@gmail.com | Email-ID is not valid      |

  Scenario Outline: Validate Password field will show error message when the field does not have any inputted data or the field has invalid data
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input data to Password field with "<password>"
    Then  I should see an error message "<errorMessage>" for the Password field of New Customer form
    Examples:
      | password | errorMessage               |
      |          | Password must not be blank |

  Scenario Outline: Validate I can add a new customer successfully
    Given I login to Guru99Bank Page and access New Customer function from the left menu
    When  I input new customer with information "<customerName>", "<gender>", "<DOB>", "<address>" , "<city>", "<state>", "<pin>", "<mobile>", "<email>", "<password>"
    And   I click Submit Button
    Then  I should be transferred to Customer Registered Message page and on the page the message "Customer Registered Successfully!!!" is displayed
    And   Information of new customer: "<customerName>", "<gender>", "<DOB>", "<address>" , "<city>", "<state>", "<pin>", "<mobile>", "<email>" should be displayed on table Customer Registered Successfully

    Examples:
      | customerName | gender | DOB         | address        | city        | state        | pin    | mobile    | email                  | password
      | John         | Male   | 16-Mar-1999 | AddressTesting | CityTesting | StateTesting | 123456 | 123456789 | Testing@mailinator.com | 1234@ABC


