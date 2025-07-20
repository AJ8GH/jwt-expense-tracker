Feature: Access token

  Scenario: Can authenticate
    Given party "PTY1" is created
    When an auth request is made for PTY1
    Then the response status is 201
    And the response has access and refresh tokens

  Scenario: Authenticated user can access secured endpoint
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    When authenticated GET request for PTY1 is made to "/expenses"
    Then the response status is 200

  Scenario: Refresh token generates new access token
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    And an auth refresh request is made for PTY1 returning access token "AT2"
    When GET request is made to "/expenses" with token AT2
    Then the response status is 200

  Scenario: Unauthenticated request returns 403
    When GET request is made to "/expenses" with token null
    Then the response status is 403
