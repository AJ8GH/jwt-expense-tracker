Feature: Access token

  Scenario: Can authenticate
    Given party "PTY1" is created
    When an auth request is made for PTY1
    Then the response status is 201
    And the response has access and refresh tokens

  Scenario: Authenticated user can access secured endpoint
    Given party "PTY1" is authenticated
    When authenticated GET request for PTY1 is made to "/actuator/info"
    Then the response status is 200
