Feature: Create party

  Scenario: Happy path
    When party "PTY1" is created
    Then the response status is 201
    And PTY1 is stored
    And PTY1 is returned

  Scenario: Username exists
    Given the following create party request is sent
      | username | password   | role |
      | a-name   | a-password | USER |
    When  the following create party request is sent
      | username | password   | role |
      | a-name   | a-password | USER |
    Then the response status is 409
