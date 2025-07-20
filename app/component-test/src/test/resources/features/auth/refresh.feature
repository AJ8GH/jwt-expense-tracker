Feature: Refresh token

  Scenario: Refresh token generates new access token
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    And an auth refresh request is made for PTY1 returning access token "AT2"
    When GET request is made to "/expenses" with token AT2
    Then the response status is 200

  Scenario: Access used as refresh token returns 403
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    When an auth refresh request is made with token AT1
    Then the response status is 403

  Scenario: Refresh token generates new access token
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    And an auth refresh request is made for PTY1 returning access token "AT2"
    When GET request is made to "/expenses" with token AT2
    Then the response status is 200

  Scenario: Refresh token for non existent party returns 403
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    And party PTY1 is deleted
    When an auth refresh request is made with token RT1
    Then the response status is 403
