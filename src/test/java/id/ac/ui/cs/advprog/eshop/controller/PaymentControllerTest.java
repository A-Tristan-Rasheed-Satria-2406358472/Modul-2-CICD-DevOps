package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    private static final String PAYMENT_ADMIN_LIST_PATH = "/payment/admin/list";
    private static final String EXISTING_PAYMENT_ID = "payment-1";
    private static final String MISSING_PAYMENT_ID = "missing-payment";
    private static final String VOUCHER_METHOD = "Voucher Code";
    private static final String REJECTED_STATUS = "REJECTED";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Product");
        product.setProductQuantity(1);

        Order order = new Order("order-1", List.of(product), 1708560000L, "Safira Sudrajat");
        payment = new Payment(EXISTING_PAYMENT_ID, order, VOUCHER_METHOD, "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));
    }

    @Test
    void paymentDetailForm_returnsDetailView() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetail"));
    }

    @Test
    void paymentDetailPage_returnsDetailViewWithPayment() throws Exception {
        when(paymentService.getPayment(EXISTING_PAYMENT_ID)).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/{paymentId}", EXISTING_PAYMENT_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void paymentAdminListPage_returnsListView() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(List.of(payment));

        mockMvc.perform(get(PAYMENT_ADMIN_LIST_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void paymentAdminDetailPage_whenPaymentExists_returnsDetailView() throws Exception {
        when(paymentService.getPayment(EXISTING_PAYMENT_ID)).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/{paymentId}", EXISTING_PAYMENT_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void paymentAdminDetailPage_whenPaymentMissing_redirectsToList() throws Exception {
        when(paymentService.getPayment(MISSING_PAYMENT_ID)).thenReturn(null);

        mockMvc.perform(get("/payment/admin/detail/{paymentId}", MISSING_PAYMENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(PAYMENT_ADMIN_LIST_PATH));
    }

    @Test
    void setPaymentStatus_whenPaymentExists_returnsUpdatedDetailView() throws Exception {
        Payment updatedPayment = new Payment(EXISTING_PAYMENT_ID, payment.getOrder(), VOUCHER_METHOD, REJECTED_STATUS, payment.getPaymentData());
        when(paymentService.getPayment(EXISTING_PAYMENT_ID)).thenReturn(payment);
        when(paymentService.setStatus(payment, REJECTED_STATUS)).thenReturn(updatedPayment);

        mockMvc.perform(post("/payment/admin/set-status/{paymentId}", EXISTING_PAYMENT_ID)
                        .param("status", REJECTED_STATUS))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));

        verify(paymentService).setStatus(payment, REJECTED_STATUS);
    }

    @Test
    void setPaymentStatus_whenPaymentMissing_redirectsToList() throws Exception {
        when(paymentService.getPayment(MISSING_PAYMENT_ID)).thenReturn(null);

        mockMvc.perform(post("/payment/admin/set-status/{paymentId}", MISSING_PAYMENT_ID)
                        .param("status", REJECTED_STATUS))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(PAYMENT_ADMIN_LIST_PATH));
    }
}
