package id.ac.ui.cs.advprog.eshop.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.bonigarcia.seljup.SeleniumJupiter;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class HomePageFunctionalTest {

  @LocalServerPort
  private int serverPort;

  @Value("${app.baseUrl:http://localhost}")
  private String testBaseUrl;

  private String baseUrl;

  @BeforeEach
  @SuppressWarnings("unused")
  void setupTest() {
    baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
  }

  @Test
  void pageTitle_isCorrect(ChromeDriver driver) {
    driver.get(baseUrl);
    String pageTitle = driver.getTitle();

    assertEquals("ADV Shop", pageTitle);
  }

  @Test
  void welcomeMessage_homePage_isCorrect(ChromeDriver driver) {
    driver.get(baseUrl);
    String welcomeMessage = driver.findElement(By.tagName("h3")).getText();

    assertEquals("Welcome", welcomeMessage);
  }

  @Test
  void productListButton_pointsToProductListPage(ChromeDriver driver) {
    driver.get(baseUrl);
    String href = driver.findElement(By.id("product-list-button")).getDomProperty("href");

    assertEquals(baseUrl + "/product/list", href);
  }
}
