package id.ac.ui.cs.advprog.eshop.functional;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.bonigarcia.seljup.SeleniumJupiter;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

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
    void user_canCreateProduct_andSeeItInList(ChromeDriver driver) throws Exception {
        String name = "Functional Test Product";
        String qty = "13";

        driver.get(baseUrl + "/product/create");

        driver.findElement(By.id("nameInput")).sendKeys(name);
        driver.findElement(By.id("quantityInput")).sendKeys(qty);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.titleIs("Product List"));

        List<WebElement> matches = driver.findElements(By.xpath("//td[text()='" + name + "']"));
        assertFalse(matches.isEmpty(), "Created product should appear in the product list");

        WebElement qtyCell = driver.findElement(By.xpath("//tr[td[text()='" + name + "']]/td[3]"));
        assertEquals(qty, qtyCell.getText());
    }
}
