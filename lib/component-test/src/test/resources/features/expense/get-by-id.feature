Feature: Get all expenses

  Background:
    Given party "PTY1" is authenticated with tokens: access "AT1", refresh "RT1"

  Scenario: Happy path returns 200
    And the following create expense request "EXP1" is sent with token AT1
      | category | amount | date       |
      | FOOD     | 100.00 | 2025-01-01 |
    And the response status is 201
    When a get expense EXP1 request is sent with token AT1
    Then the response status is 200
    And the following expense is returned
      | id   | partyId | category | amount | date       |
      | EXP1 | PTY1    | FOOD     | 100.00 | 2025-01-01 |

  Scenario: Returns 404 when expense not found by ID
    When an erroneous get expense EXP1 request is sent with token AT1
    Then the response status is 404
    And the response error message contains "Expense EXP1 not found"

  Scenario: Returns 401 when token not authorised to access expense ID
    Given party "PTY2" is authenticated with tokens: access "AT2", refresh "RT2"
    And the following create expense request "EXP1" is sent with token AT1
      | category | amount | date       |
      | FOOD     | 100.00 | 2025-01-01 |
    And the response status is 201
    When an erroneous get expense EXP1 request is sent with token AT2
    Then the response status is 401
    And the response error message contains "Party PTY2 is unauthorized to access resource of type EXPENSE, id EXP1"
