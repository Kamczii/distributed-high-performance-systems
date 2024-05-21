import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Navigating Offers Webpage")
public class ATests {
    private static final String ADDRESS = "http://localhost:4200";
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
    public void searchesOffers() {
        tools.clickSearch();
        List<WebElement> offerCards = tools.findOffers();
        int n = offerCards.size();
        assertTrue(n > 0);
    }

    @Test
    @DisplayName("Buys offers from the search list")
    public void buysOffers() {
        tools.clickSearch();
        List<WebElement> offerCards = tools.findOffers();
        assertFalse(offerCards.isEmpty(), "No offers found in search results.");

        for (int i = 0; i < 30; i++) {
            tools.clickOffer(i);
            tools.buyOffer();
            driver.get(ADDRESS); // Back to the main page
            tools.clickSearch();
        }
    }

    @AfterEach
    void afterEach() {
        driver.quit();
    }
}
