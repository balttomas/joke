Feature: Get random single joke
  Scenario: client makes call to GET random joke
    When client calls GET "/jokes/random"
    Then client receives status code 200
    And response contains joke value
