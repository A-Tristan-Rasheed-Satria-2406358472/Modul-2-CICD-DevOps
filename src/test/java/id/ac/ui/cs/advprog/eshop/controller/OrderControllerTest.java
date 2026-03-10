package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

  private static final String ORDER_HISTORY_PATH = "/order/history";
  private static final String ORDER_HISTORY_SEARCH_PATH = "/order/history/search";
  private static final String ORDER_HISTORY_RESULT_PATH = "/order/history/result";
  private static final String ORDER_PAY_PATH = "/order/pay/{orderId}";
  private static final String ORDER_PAY_PROCESS_PATH = "/order/pay/{orderId}/process";
  private static final String ORDER_PAY_RESULT_PATH = "/order/pay-result";
  private static final String EXISTING_ORDER_ID = "order-1";
  private static final String MISSING_ORDER_ID = "missing-order";
  private static final String TEST_AUTHOR = "Safira Sudrajat";
  private static final String VOUCHER_METHOD = "Voucher Code";
  private static final String VOUCHER_CODE_KEY = "voucherCode";
  private static final String VALID_VOUCHER_CODE = "ESHOP1234ABC5678";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  @MockBean
  private PaymentService paymentService;

  private Order order;

  @BeforeEach
  void setUp() {
    Product product = new Product();
    product.setProductId("product-1");
    product.setProductName("Product");
    product.setProductQuantity(1);

    order = new Order(EXISTING_ORDER_ID, List.of(product), 1708560000L, TEST_AUTHOR);
  }

  @Test
  void createOrderPage_returnsCreateView() throws Exception {
    mockMvc.perform(get("/order/create"))
        .andExpect(status().isOk())
        .andExpect(view().name("CreateOrder"));
  }

  @Test
  void orderHistoryPage_returnsHistoryView() throws Exception {
    mockMvc.perform(get(ORDER_HISTORY_PATH))
        .andExpect(status().isOk())
        .andExpect(view().name("OrderHistory"));
  }

  @Test
  void orderHistoryPost_redirectsToHistoryResult() throws Exception {
    mockMvc.perform(post(ORDER_HISTORY_SEARCH_PATH).param("author", TEST_AUTHOR))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern(ORDER_HISTORY_RESULT_PATH + "?author=*"));
  }

  @Test
  void orderHistoryResult_returnsHistoryListView() throws Exception {
    when(orderService.findAllByAuthor(TEST_AUTHOR)).thenReturn(List.of(order));

    mockMvc.perform(get(ORDER_HISTORY_RESULT_PATH).param("author", TEST_AUTHOR))
        .andExpect(status().isOk())
        .andExpect(view().name("OrderHistoryList"))
        .andExpect(model().attributeExists("author"))
        .andExpect(model().attributeExists("orders"));
  }

  @Test
  void payOrderPage_whenOrderExists_returnsPayView() throws Exception {
    when(orderService.findById(EXISTING_ORDER_ID)).thenReturn(order);

    mockMvc.perform(get(ORDER_PAY_PATH, EXISTING_ORDER_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("OrderPay"))
        .andExpect(model().attributeExists("order"));
  }

  @Test
  void payOrderPage_whenOrderMissing_redirectsToHistory() throws Exception {
    when(orderService.findById(MISSING_ORDER_ID)).thenReturn(null);

    mockMvc.perform(get("/order/pay/{orderId}", MISSING_ORDER_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ORDER_HISTORY_PATH));
  }

  @Test
  void payOrderPost_whenOrderExists_redirectsToPayResult() throws Exception {
    Payment payment = new Payment("payment-1", order, VOUCHER_METHOD, "SUCCESS",
        Map.of(VOUCHER_CODE_KEY, VALID_VOUCHER_CODE));
    when(orderService.findById(EXISTING_ORDER_ID)).thenReturn(order);
    when(paymentService.addPayment(eq(order), eq(VOUCHER_METHOD), org.mockito.ArgumentMatchers.anyMap()))
        .thenReturn(payment);

    mockMvc.perform(post(ORDER_PAY_PROCESS_PATH, EXISTING_ORDER_ID)
        .param("method", VOUCHER_METHOD)
        .param(VOUCHER_CODE_KEY, VALID_VOUCHER_CODE))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ORDER_PAY_RESULT_PATH + "?paymentId=payment-1"));

    ArgumentCaptor<Map<String, String>> paymentDataCaptor = ArgumentCaptor.forClass(Map.class);
    verify(paymentService).addPayment(eq(order), eq(VOUCHER_METHOD), paymentDataCaptor.capture());
    assertFalse(paymentDataCaptor.getValue().containsKey("method"));
    assertEquals(VALID_VOUCHER_CODE, paymentDataCaptor.getValue().get(VOUCHER_CODE_KEY));
  }

  @Test
  void payOrderResult_showsPaymentId() throws Exception {
    mockMvc.perform(get(ORDER_PAY_RESULT_PATH).param("paymentId", "payment-1"))
        .andExpect(status().isOk())
        .andExpect(view().name("OrderPayResult"))
        .andExpect(model().attribute("paymentId", "payment-1"));
  }

  @Test
  void payOrderPost_whenOrderMissing_redirectsToHistory() throws Exception {
    when(orderService.findById(MISSING_ORDER_ID)).thenReturn(null);

    mockMvc.perform(post(ORDER_PAY_PROCESS_PATH, MISSING_ORDER_ID)
        .param("method", VOUCHER_METHOD)
        .param(VOUCHER_CODE_KEY, VALID_VOUCHER_CODE))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(ORDER_HISTORY_PATH));
  }
}
