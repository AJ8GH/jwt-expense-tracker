Feature: Access token

  Scenario: Can authenticate
    Given party "PTY1" is created
    When an auth request is made for PTY1
    Then the response status is 201
    And the response has access and refresh tokens

  Scenario: Authenticated user can access secured endpoint
    Given party "PTY1" is authenticated
    When authenticated GET request for PTY1 is made to "/expenses"
    Then the response status is 200

  Scenario: Refresh token generates new access token
    Given party "PTY1" is authenticated
    And an auth refresh request is made for PTY1
    When GET request using refreshed access token for PTY1 is made to "/expenses"
    Then the response status is 200
