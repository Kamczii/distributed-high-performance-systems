import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Acceptance tests for RSWW project")
public class ATests {

    @Test
    @DisplayName("Reserving offer works")
    public void reservingOffer(){
        int offerPosition = 11;
        WebClient clientA = new WebClient(Constants.ADDRESS);
        clientA.wait(5);
        clientA.clickOffer(offerPosition);
        clientA.buyAndCancelOffer();

        int n = 0;
        int s = 0;
        List<WebElement> offerLogs;
        while (n == 0 && s < 360) {
            offerLogs = clientA.getOfferUpdateLog();
            n = offerLogs.size();
            clientA.wait(1);
            s++;
        }

        clientA.wait(2);
        clientA.buyAndCancelOffer();
        clientA.wait(2);
        clientA.buyAndCancelOffer();
        String status = clientA.getOfferStatus();
        s = 0;
        while (!status.equals("RESERVED") && s < 360) {
            status = clientA.getOfferStatus();
            clientA.wait(1);
            s++;
        }

        assertEquals(status, "RESERVED");

        clientA.exit();
    }

    //Do not work:
//    @Test
//    @DisplayName("Buying offer several time changes offer status to closed")
//    public void buyingOfferSeveralTimesChangesStatusToClosed() {
//        int offerPosition = 2;
//        WebClient client = new WebClient(Constants.ADDRESS);
//        client.wait(10);
//        client.clickOffer(offerPosition);
//
//        String status = client.getOfferStatus();
//        assertEquals("OPEN", status);
//
//        client.buyUntilOfferCloses();
//
//        status = client.getOfferStatus();
//        assertEquals("ENDED", status);
//        client.exit();
//    }

    @Test
    @DisplayName("Reserving offer by client B generates prompt in offer page of client A")
    public void reservingOfferByClientBGeneratesPromptForClientA() {
        int offerPosition = 13;
        WebClient clientA = new WebClient(Constants.ADDRESS);
        clientA.wait(5);
        clientA.clickOffer(offerPosition);

        WebClient clientB = new WebClient(Constants.ADDRESS);
        clientB.wait(5);
        clientB.clickOffer(offerPosition);
        clientB.buyOffer();

        int n = 0;
        int s = 0;
        List<WebElement> offerLogs;
        while (n == 0 && s < 360) {
            offerLogs = clientA.getOfferUpdateLog();
            n = offerLogs.size();
            clientA.wait(1);
            s++;
        }

        assertTrue(n > 0);

        clientA.exit();
        clientB.exit();
    }
}