package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

  private static final String ORDER_ID = "13652556-012a-4c07-b546-54eb1396d79b";
  private static final String AUTHOR = "Safira Sudrajat";
  private static final long ORDER_TIME = 1708560000L;

  private List<Product> products;

  @BeforeEach
  void setUp() {
    this.products = new ArrayList<>();

    Product product1 = new Product();
    product1.setProductId("eb558e9f-1c39-4600-8860-71af6af63bd6");
    product1.setProductName("Sampo Cap Bambang");
    product1.setProductQuantity(2);

    Product product2 = new Product();
    product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
    product2.setProductName("Sabun Cap Usep");
    product2.setProductQuantity(1);

    this.products.add(product1);
    this.products.add(product2);
  }

  @Test
  void testCreateOrderEmptyProduct() {
    this.products.clear();

    assertThrows(IllegalArgumentException.class, () -> {
      Order order = new Order(
          ORDER_ID,
          this.products,
          ORDER_TIME,
          AUTHOR);
    });
  }

  @Test
  void testCreateOrderDefaultStatus() {
    Order order = new Order(
        ORDER_ID,
        this.products,
        ORDER_TIME,
        AUTHOR);

    assertSame(this.products, order.getProducts());
    assertEquals(2, order.getProducts().size());
    assertEquals("Sampo Cap Bambang", order.getProducts().get(0).getProductName());
    assertEquals("Sabun Cap Usep", order.getProducts().get(1).getProductName());

    assertEquals(ORDER_ID, order.getId());
    assertEquals(ORDER_TIME, order.getOrderTime());
    assertEquals(AUTHOR, order.getAuthor());
    assertEquals(OrderStatus.WAITING_PAYMENT.getValue(), order.getStatus());
  }

  @Test
  void testCreateOrderSuccessStatus() {
    Order order = new Order(
        ORDER_ID,
        this.products,
        ORDER_TIME,
        AUTHOR,
        OrderStatus.SUCCESS.getValue());

    assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
  }

  @Test
  void testCreateOrderInvalidStatus() {
    assertThrows(IllegalArgumentException.class, () -> {
      Order order = new Order(
          ORDER_ID,
          this.products,
          ORDER_TIME,
          AUTHOR,
          "MEOW");
    });
  }

  @Test
  void testSetStatusToCancelled() {
    Order order = new Order(
        ORDER_ID,
        this.products,
        ORDER_TIME,
        AUTHOR);

    order.setStatus(OrderStatus.CANCELLED.getValue());
    assertEquals(OrderStatus.CANCELLED.getValue(), order.getStatus());
  }

  @Test
  void testSetStatusToInvalidStatus() {
    Order order = new Order(
        ORDER_ID,
        this.products,
        ORDER_TIME,
        AUTHOR);

    assertThrows(IllegalArgumentException.class, () -> order.setStatus("MEOW"));
  }
}
