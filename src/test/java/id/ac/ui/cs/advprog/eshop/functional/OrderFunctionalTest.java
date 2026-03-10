package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createOrderPage_isAccessible(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");

        assertEquals("Create Order", driver.getTitle());
        assertEquals("Create Order", driver.findElement(By.tagName("h1")).getText());
    }

    @Test
    void orderHistoryPage_isAccessible(ChromeDriver driver) {
        driver.get(baseUrl + "/order/history");

        assertEquals("Order History", driver.getTitle());
        assertEquals("Order History", driver.findElement(By.tagName("h1")).getText());
    }

    @Test
    void user_canSubmitOrderHistoryForm(ChromeDriver driver) {
        driver.get(baseUrl + "/order/history");

        driver.findElement(By.id("author")).sendKeys("Safira Sudrajat");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertEquals("Order History List", driver.getTitle());
        assertEquals("Order History Result", driver.findElement(By.tagName("h1")).getText());
    }
}
