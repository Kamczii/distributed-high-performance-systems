import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebClient {
    private WebDriver driver;

    public WebClient(String address){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
        driver.get(address);
    }

    public List<WebElement> getOfferUpdateLog(){
        WebElement updatePage = driver.findElement(By.className("updates-list"));
        return updatePage.findElements(By.className("card"));
    }

    public void buyOffer(){
        WebElement offerBox = driver.findElement(By.className("offer-short-info"));
        WebElement buyButton = offerBox.findElement(By.xpath("button"));
        buyButton.click();
        WebElement acceptButton = driver.findElement(By.className("modal-buttons")).findElements(By.tagName("button")).get(0);
        acceptButton.click();
    }

    public void buyAndCancelOffer(){
        WebElement offerBox = driver.findElement(By.className("offer-short-info"));
        WebElement buyButton = offerBox.findElement(By.xpath("button"));
        buyButton.click();
        WebElement acceptButton = driver.findElement(By.className("modal-buttons")).findElements(By.tagName("button")).get(1);
        acceptButton.click();
    }

    public void waitForLogToAppear(){
        List<WebElement> offerLogs = getOfferUpdateLog();
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(d -> !offerLogs.isEmpty());
    }

    public String getOfferStatus(){
        WebElement offerBox = driver.findElement(By.className("offer-short-info"));
        WebElement statusElement = offerBox.findElements(By.tagName("p")).get(0);
        return statusElement.getText().split(": ")[1];
    }

//    public void loadOffersFromTo(int fromPosition, int toPosition){
//        WebElement selectFromElement = driver.findElement(By.className(""));
//        //click
//        WebElement selectToElement = driver.findElement(By.className(""));
//        //click
//        WebElement searchButton = driver.findElement(By.className(""));
//        //click
//    }
//
//    public void changeNumberOfPeopleInOffer(int n){
//        WebElement selectNumPeopleElement = driver.findElement(By.className(""));
//        //click
//    }

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

    public void buyUntilOfferCloses(){
        String status = getOfferStatus();
        int i = 0;
        wait(5);
        while(!status.equals("ENDED") && i < 20){
            buyOffer();
            status = getOfferStatus();
            i++;
        }
    }

    public void wait(int i){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(i));
    }

//    public WebElement getLastOfferUpdate(){
//        WebElement updatePage = driver.findElement(By.className("updates-list"));
//        List<WebElement> lastUpdate = updatePage.findElements(By.className("card"));
//        return lastUpdate.get(0);
//    }

    public void exit(){
        driver.close();
    }

}
