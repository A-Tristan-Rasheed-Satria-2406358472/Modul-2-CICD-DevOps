package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRepositoryTest {

    OrderRepository orderRepository;
    List<Order> orders;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-4600-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        orders = new ArrayList<>();
        Order order1 = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                products,
                1708560000L,
                "Safira Sudrajat"
        );
        orders.add(order1);

        Order order2 = new Order(
                "f79e15bb-4b15-42f4-ee8c-c3af385fb078",
                products,
                1708570000L,
                "Safira Sudrajat"
        );
        orders.add(order2);

        Order order3 = new Order(
                "e35e4f49-9eff-4da8-94b7-8ee697ecbfc1",
                products,
                1708573000L,
                "Bambang Sudrajat"
        );
        orders.add(order3);
    }

    @Test
    void testSaveCreate() {
        Order order = orders.get(1);
        Order result = orderRepository.save(order);
        Order findResult = orderRepository.findById(orders.get(1).getId());

        assertEquals(order.getId(), result.getId());
        assertEquals(order.getId(), findResult.getId());
        assertEquals(order.getOrderTime(), findResult.getOrderTime());
        assertEquals(order.getAuthor(), findResult.getAuthor());
        assertEquals(order.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        Order order = orders.get(1);
        orderRepository.save(order);

        Order updatedOrder = new Order(
                order.getId(),
                order.getProducts(),
                1708580000L,
                "Bambang Sudrajat"
        );

        Order result = orderRepository.save(updatedOrder);
        Order findResult = orderRepository.findById(order.getId());

        assertEquals(updatedOrder.getId(), result.getId());
        assertEquals(updatedOrder.getId(), findResult.getId());
        assertEquals(updatedOrder.getOrderTime(), findResult.getOrderTime());
        assertEquals(updatedOrder.getAuthor(), findResult.getAuthor());
        assertEquals(updatedOrder.getStatus(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdCorrect() {
        for (Order order : orders) {
            orderRepository.save(order);
        }

        Order result = orderRepository.findById(orders.get(0).getId());

        assertEquals(orders.get(0).getId(), result.getId());
        assertEquals(orders.get(0).getOrderTime(), result.getOrderTime());
        assertEquals(orders.get(0).getAuthor(), result.getAuthor());
        assertEquals(orders.get(0).getStatus(), result.getStatus());
    }

    @Test
    void testFindByIdIfIdWrong() {
        for (Order order : orders) {
            orderRepository.save(order);
        }

        Order result = orderRepository.findById("never-used-id");

        assertNull(result);
    }

    @Test
    void testFindAllByAuthorIfAuthorCorrect() {
        for (Order order : orders) {
            orderRepository.save(order);
        }

        List<Order> orderList = orderRepository.findAllByAuthor(
                orders.get(1).getAuthor()
        );

        assertEquals(2, orderList.size());
    }

    @Test
    void testFindAllByAuthorIfAllLowercase() {
        orderRepository.save(orders.get(1));

        List<Order> orderList = orderRepository.findAllByAuthor(
                orders.get(1).getAuthor().toLowerCase(Locale.ROOT)
        );

        assertTrue(orderList.isEmpty());
    }
}
