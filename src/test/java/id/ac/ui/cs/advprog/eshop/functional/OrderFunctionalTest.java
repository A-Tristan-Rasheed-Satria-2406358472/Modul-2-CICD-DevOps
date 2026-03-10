package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class OrderFunctionalTest extends FunctionalTestBase {

    private static final String ORDER_ID = "order-functional-pay-1";

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    @Autowired
    private OrderRepository orderRepository;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-functional-order-1");
        product.setProductName("Functional Order Product");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order(
                ORDER_ID,
                products,
                1708560000L,
                "Safira Sudrajat"
        );
        orderRepository.save(order);
    }

    @Test
    void createOrderPage_isAccessible() {
        ChromeDriver driver = createDriver();
        try {
            driver.get(baseUrl + "/order/create");

            assertEquals("Create Order", driver.getTitle());
            assertEquals("Create Order", driver.findElement(By.tagName("h1")).getText());
        } finally {
            driver.quit();
        }
    }

    @Test
    void orderHistoryPage_isAccessible() {
        ChromeDriver driver = createDriver();
        try {
            driver.get(baseUrl + "/order/history");

            assertEquals("Order History", driver.getTitle());
            assertEquals("Order History", driver.findElement(By.tagName("h1")).getText());
        } finally {
            driver.quit();
        }
    }

    @Test
    void user_canSubmitOrderHistoryForm() {
        ChromeDriver driver = createDriver();
        try {
            driver.get(baseUrl + "/order/history");

            driver.findElement(By.id("author")).sendKeys("Safira Sudrajat");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.titleIs("Order History List"));

            assertEquals("Order History List", driver.getTitle());
            assertEquals("Order History Result", driver.findElement(By.tagName("h1")).getText());
        } finally {
            driver.quit();
        }
    }

    @Test
    void payOrderPage_isAccessibleForExistingOrder() {
        ChromeDriver driver = createDriver();
        try {
            driver.get(baseUrl + "/order/pay/" + ORDER_ID);

            assertEquals("Order Payment", driver.getTitle());
            assertEquals("Order Payment", driver.findElement(By.tagName("h1")).getText());
        } finally {
            driver.quit();
        }
    }

    @Test
    void user_canSubmitPaymentForOrder() {
        ChromeDriver driver = createDriver();
        try {
            driver.get(baseUrl + "/order/pay/" + ORDER_ID);

            Select paymentMethod = new Select(driver.findElement(By.id("method")));
            paymentMethod.selectByValue("Voucher Code");
            driver.findElement(By.id("voucherCode")).sendKeys("ESHOP1234ABC5678");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.titleIs("Order Payment Result"));

            assertEquals("Order Payment Result", driver.getTitle());
            assertEquals("Order Payment Result", driver.findElement(By.tagName("h1")).getText());
            Assertions.assertFalse(driver.findElement(By.id("payment-id")).getText().isBlank());
        } finally {
            driver.quit();
        }
    }
}
