Feature: Get random single joke by category
  Scenario: client makes call to GET random joke by category
    When client calls GET "/jokes/random/dev"
    Then client receives status code 200
    And response contains joke value

  Scenario: client makes call to GET random joke by non-existent category
    When client calls GET "/jokes/random/nonexistent"
    Then client receives status code 404
