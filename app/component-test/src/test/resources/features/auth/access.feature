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

  Scenario: Unauthenticated request returns 401
    When GET request is made to "/expenses" with token null
    Then the response status is 401

  Scenario: Refresh used as access token returns 401
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    When GET request is made to "/expenses" with token RT1
    Then the response status is 401

  Scenario: Auth request for unknown party returns 401
    When the following auth request is made
      | username |
      | noone    |
    Then the response status is 401

  Scenario: Access token for non existent party returns 401
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    And party PTY1 is deleted
    When GET request is made to "/expenses" with token AT1
    Then the response status is 401

  Scenario: Invalid bearer format returns 401
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    When GET request is made to "/expenses" with token AT1 and invalid bearer prefix
    Then the response status is 401
