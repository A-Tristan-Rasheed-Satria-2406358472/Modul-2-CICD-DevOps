package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PaymentFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private String baseUrl;
    private String paymentId;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-functional-1");
        product.setProductName("Functional Product");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order(
                "order-functional-1",
                products,
                1708560000L,
                "Safira Sudrajat"
        );
        orderRepository.save(order);

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(
                "payment-functional-1",
                order,
                "Voucher Code",
                "SUCCESS",
                paymentData
        );
        paymentRepository.save(payment);
        paymentId = payment.getId();
    }

    @Test
    void paymentDetailForm_isAccessible(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/detail");

        assertEquals("Payment Detail", driver.getTitle());
        assertEquals("Payment Detail", driver.findElement(By.tagName("h1")).getText());
    }

    @Test
    void paymentDetailPage_showsPaymentById(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/detail/" + paymentId);

        assertEquals("Payment Detail", driver.getTitle());
        assertEquals(paymentId, driver.findElement(By.id("payment-id")).getText());
    }

    @Test
    void paymentAdminListPage_showsAllPayments(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/admin/list");

        assertEquals("Payment List", driver.getTitle());
        assertTrue(driver.getPageSource().contains(paymentId));
    }

    @Test
    void paymentAdminDetailPage_showsPaymentData(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/admin/detail/" + paymentId);

        assertEquals("Payment Admin Detail", driver.getTitle());
        assertEquals(paymentId, driver.findElement(By.id("payment-id")).getText());
    }

    @Test
    void admin_canSetPaymentStatus(ChromeDriver driver) {
        driver.get(baseUrl + "/payment/admin/detail/" + paymentId);

        Select statusSelect = new Select(driver.findElement(By.id("statusInput")));
        statusSelect.selectByValue("REJECTED");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertEquals("Payment Admin Detail", driver.getTitle());
        assertEquals("REJECTED", driver.findElement(By.id("payment-status")).getText());
    }
}
