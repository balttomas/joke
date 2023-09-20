Feature: Get random single joke by category
  Scenario: client makes call to GET random joke by category
    When client calls GET "/jokes/search?query=hospital"
    Then client receives status code 200
#    And response contains joke value
