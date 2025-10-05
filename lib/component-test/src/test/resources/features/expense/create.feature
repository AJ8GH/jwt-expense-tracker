Feature: Create expense

  Scenario: Create single expense - returns 201
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    When the following create expense request "EXP1" is sent with token AT1
      | category | amount | date       |
      | FOOD     | 100.00 | 2025-01-01 |
    Then the response status is 201
    And the following expense is stored
      | id   | partyId | category | amount | date       |
      | EXP1 | PTY1    | FOOD     | 100.00 | 2025-01-01 |
    And the following expense is returned
      | id   | partyId | category | amount | date       |
      | EXP1 | PTY1    | FOOD     | 100.00 | 2025-01-01 |

  Scenario: Create multiple expenses - returns 201
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"
    When the following create expenses request "EXP1, EXP2" is sent with token AT1
      | category | amount | date       |
      | FOOD     | 100.00 | 2025-01-01 |
      | BILLS    | 50.00  | 2025-02-01 |
    Then the response status is 201
    And the following expense is stored
      | id   | partyId | category | amount | date       |
      | EXP1 | PTY1    | FOOD     | 100.00 | 2025-01-01 |
    And the following expenses are returned
      | id   | partyId | category | amount | date       |
      | EXP1 | PTY1    | FOOD     | 100.00 | 2025-01-01 |
      | EXP2 | PTY1    | BILLS    | 50.00  | 2025-02-01 |
