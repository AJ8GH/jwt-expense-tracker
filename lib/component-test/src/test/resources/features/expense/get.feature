Feature: Get all expenses

  Scenario: Happy path returns 200 with no expenses
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    When a get all expenses request is sent with token AT1
    Then the response status is 200
    And an empty expenses response is returned

  Scenario: Happy path returns 200 with single expense
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    And the following create expense request "EXP1" is sent with token AT1
      | category | amount | date       |
      | FOOD     | 100.00 | 2025-01-01 |
    And the response status is 201
    When a get all expenses request is sent with token AT1
    Then the response status is 200
    And the following expenses are returned
      | id   | partyId | category | amount | date       |
      | EXP1 | PTY1    | FOOD     | 100.00 | 2025-01-01 |

  Scenario: Happy path returns 200 with multiple expenses
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    And the following create expenses request "EXP1, EXP2" is sent with token AT1
      | category | amount | date       |
      | FOOD     | 100.00 | 2025-01-01 |
      | BILLS    | 50.00  | 2025-01-02 |
    And the response status is 201
    When a get all expenses request is sent with token AT1
    Then the response status is 200
    And the following expenses are returned
      | id   | partyId | category | amount | date       |
      | EXP1 | PTY1    | FOOD     | 100.00 | 2025-01-01 |
      | EXP2 | PTY1    | BILLS    | 50.00  | 2025-01-02 |
