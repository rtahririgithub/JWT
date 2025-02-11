Feature: Greeting functionality

  Tests '/greeting' resource.

Background:
* url appUrl

Scenario: Get default greeting

    Given path '/'
    When method get
    Then status 200
    And match response == { id: '#number', content: 'Hello, World!' }
    
Scenario: Get custom greeting for the provided name

    Given path '/'
    And param name = 'Novak'
    When method get
    Then status 200
    And match response == { id: '#number', content: 'Hello, Novak!' }

