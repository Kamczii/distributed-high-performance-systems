import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Navigating Offers Webpage")
public class ATests {
    private static final String ADDRESS = "http:/localhost:4200";
    private WebDriver driver;
    private WebTools tools;

    @BeforeEach
    void beforeEach() {
        driver = new ChromeDriver();
        tools = new WebTools(driver);
        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
        driver.get(ADDRESS);
    }

    @Test
    @DisplayName("Searches offers")
    public void searchesOffers(){
        beforeEach();
        tools.clickSearch();
        List<WebElement> offerCards = tools.findOffers();
        int n = offerCards.size();
        assertTrue(n > 0);
        afterEach();
    }

    @Test
    @DisplayName("AAA") //TODO
    public void buysOffers(){
        beforeEach();
        tools.clickSearch();
        tools.clickOffer(1); // goes to offer page
        tools.buyOffer();
        driver.get(ADDRESS); // back to main page
        assertTrue(0 == 0); // TODO
        afterEach();
    }

    //TODO
    //offer not available after to many have bought it and there is no place in a plane
    @Test
    @DisplayName("Offer become not available")
    public void offerNotBecomeAvailable(){
        beforeEach();

        for(int i = 0; i < 5; i++){
            tools.clickOffer(1); // goes to offer page
            tools.buyOffer();
            driver.get(ADDRESS); // back to main page
        }

        assertTrue(0 == 0); // TODO
        afterEach();
    }

    @AfterEach
    void afterEach() {
        driver.quit();
    }
}
