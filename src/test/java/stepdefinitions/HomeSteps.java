package stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HomeSteps {
    private WebDriver driver;     
    String productCode ;

    @Given("I am in the home page")
    public void i_am_in_login_page(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/webdrivers/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://www.falabella.com/");
        driver.manage().window().maximize();
    }

    @When("I click in the hot sale modal")
    public void i_click_hot_sale_modal(){
        driver.findElement(By.cssSelector("body > div.dy-modal-container.dy-act-overlay > div.dy-modal-wrapper > div")).click();
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("CMR-enlinea"));
    }

    @And("I click in the Menu option")
    public void i_click_menu_option(){
        driver.findElement(By.id("testId-HamburgerBtn-toggle")).click();
    }
    
    /*     
    * In this method, we need to hover the selected element. As we don't have a particular
    * name to do it so, we did it using offsets.
    * We defined offsets for x and y as 0, then calculate the position of the elemenent we wanted
    * to select. 
    * If the position shows either x or y as negative, we will set them back to 0 and continue.
    * This way we will hover an incorrect element instead crashing the test.
    * In the future though, we need to investigate a better way to do so.
    */
        
    @And("I hover to the third section")
    public void i_hover_third_section(){
        WebElement hoverTo = driver.findElement(By.xpath("//*[@id='scrollable-content']/div/div[2]/div/div[1]/div[2]/div[1]/div[3]"));
        Point hoverToLocation = hoverTo.getLocation();

        int xOffset = 0;
        int yOffset = 0;

        xOffset = hoverToLocation.getX() ;
        if (xOffset < 0) xOffset = 0;
        yOffset = hoverToLocation.getY();
        if (yOffset < 0) yOffset = 0;

        Actions actions = new Actions(driver);
        actions.moveByOffset(xOffset, yOffset).perform();
    }



    @And("I click the a second level category item")
    public void i_click_the_first_second_level_category(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='scrollable-content']//ul[3]/li[4]/a")));
        element.click();
    }

    @Then("I should see the items in the category")
    public void i_should_see_items_in_category(){
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("cat7500015/Cascos-Motos"));
        WebElement found = driver.findElement(By.id("testId-ProductLandingContainer-totalResults"));
        String amountFound = found.getText();
        int getAmountValue = getAmountValue(amountFound);
        System.out.println("Elements found: "+getAmountValue);
        assertTrue(getAmountValue >= 0);
    } 

    @And("I sort items by price asc")
    public void i_sort_elements_by_price(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dropDown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("testId-Dropdown-desktop-button")));
        dropDown.click();
        driver.findElement(By.id("testId-Dropdown-Precio de menor a mayor")).click();
    }
 
    @And("I add the first item to the cart")
    public void i_click_first_item(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement item = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testId-pod-112983110")));
        item.click();
        WebElement addToCartBtn = driver.findElement(By.id("testId-Pod-action-112983109"));
        addToCartBtn.click();
    }

    @And("I go to the cart")
    public void i_go_to_cart(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement cartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#linkButton")));
        cartBtn.click();
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("basket"));
    }

    @Then("I should see one item in the cart")
    public void i_should_see_item_in_cart(){
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("basket"));
        String products = driver.findElement(By.xpath("//span[@data-testid='total-products-count']")).getText();
        int numProducts = Integer.parseInt(products.split(" ")[0].replace("(", ""));
        assertTrue(numProducts > 0);
    }


    @And("I search mesa in the search bar")
    public void i_type_mesa_in_search_bar(){
        WebElement searchBar = driver.findElement(By.id("testId-SearchBar-Input"));
        searchBar.sendKeys("mesa");
        searchBar.sendKeys(Keys.ENTER);
        
    }

    @And("I click on the first result")
    public void i_click_first_result(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='testId-pod-110472238']/div[2]/a")));
        item.click();
    }

    @Then("I should be in the product details page")
    public void i_should_be_in_product_detail_page(){
        String currentUrl = driver.getCurrentUrl();
        System.out.println(currentUrl);
        assertTrue(currentUrl.contains("product"));
        this.productCode = findProductCode(currentUrl);
    }

    @And("I add the product to the cart from the product details page")
    public void i_add_product_to_cart_from_product_details_page(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement addToCartBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-button")));
        addToCartBtn.click();
    }

    @And("The product should be the same I added")
    public void product_should_be_same_added(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement productCodeCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__next']//section[1]//div/div[1]/div[2]/span")));
        
        String productCodeFound = productCodeCart.getText().split(" ")[1];
        System.out.println("PRODUCT CODE FOUND: "+productCodeFound);

        assertEquals(this.productCode, productCodeFound);
    }

    @And("I close the browser")
    public void i_close_browser(){
        driver.close();
    }

    private int getAmountValue(String amountFound) {
        String[] values = amountFound.split(" ");
        String amountValue = values[1].replace("(", "").replace(")","");
        return Integer.parseInt(amountValue);
    }

    private String findProductCode(String currentUrl) {
        String[] parsedUrl = currentUrl.split("/");
        String productCode = parsedUrl[parsedUrl.length-1];
        System.out.println("PRODUCT CODE IS: "+productCode);
        return productCode;
    }

}
