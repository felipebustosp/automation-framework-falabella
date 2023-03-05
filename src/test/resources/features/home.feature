Feature: Falabella exercises
As a user, I want to search a product in the Falabella website and add it to my cart.

Scenario: Open Falabella home page and select a product using the categories in the site.
    Given I am in the home page
    When I click in the hot sale modal 
    And I click in the Menu option
    And I hover to the third section
    And I click the a second level category item
    Then I should see the items in the category
    And I sort items by price asc
    And I add the first item to the cart
    And I go to the cart
    Then I should see one item in the cart
    And I close the browser







