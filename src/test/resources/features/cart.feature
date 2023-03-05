Feature: Falabella exercises
As a user, I want to search a product in the Falabella website and add it to my cart.

Scenario: Add a product to the cart and check it is as expected
    Given I am in the home page
    When I click in the hot sale modal 
    And I search mesa in the search bar
    And I sort items by price asc
    And I click on the first result
    Then I should be in the product details page
    And I add the product to the cart from the product details page
    And I go to the cart
    Then I should see one item in the cart
    And The product should be the same I added
    And The product price is the same as the purchase total
    And I close the browser
