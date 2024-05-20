import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WebTools {
    private WebDriver driver;

    public WebTools(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement findSearchBox(){
        return driver.findElement(By.className("search-box"));
    }

    public void clickSearch(){
        WebElement searchBox = findSearchBox();
        WebElement searchButton = searchBox.findElement(By.xpath("form/button"));
        searchButton.click();
    }

    public List<WebElement> findOffers(){
        WebElement searchBox = findSearchBox();
        List<WebElement> offerCards = searchBox.findElements(By.xpath("div/div/div"));
        return offerCards;
    }

    public void clickOffer(int i){
        List<WebElement> offerCards = findOffers();
        WebElement ithOffer = offerCards.get(i);
        WebElement link = ithOffer.findElement(By.xpath("a"));
        link.click();
    }

    public void buyOffer(){
        WebElement offerBox = driver.findElement(By.className("offer-short-info"));
        WebElement buyButton = offerBox.findElement(By.xpath("button"));
        buyButton.click();
    }
}
