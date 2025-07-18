Feature: Access token

  Scenario: Can authenticate
    Given party "PTY1" is created
      | username | password |
      | myname   | pw123    |
    When the following auth request is made
      | username | password |
      | myname   | pw123    |
    Then the response status is 201
    And the response json path "accessToken" is not empty
    And the response json path "refreshToken" is not empty
