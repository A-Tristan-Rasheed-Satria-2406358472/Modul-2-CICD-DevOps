package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    private static final String PAYMENT_ID = "payment-1";
    private static final String VOUCHER_METHOD = "Voucher Code";
    private static final String BANK_TRANSFER_METHOD = "Bank Transfer";
    private static final String PENDING_STATUS = "PENDING";
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String REJECTED_STATUS = "REJECTED";
    private static final String FAILED_STATUS = "FAILED";

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-4600-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        this.order = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                products,
                1708560000L,
                "Safira Sudrajat"
        );

        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testAddPayment() {
        doReturn(new Payment(PAYMENT_ID, this.order, VOUCHER_METHOD, PENDING_STATUS, this.paymentData))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(this.order, VOUCHER_METHOD, this.paymentData);

        assertNotNull(result);
        assertEquals(VOUCHER_METHOD, result.getMethod());
        assertEquals(this.paymentData, result.getPaymentData());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithValidVoucherCodeSetsSuccessStatus() {
        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(this.order, VOUCHER_METHOD, this.paymentData);

        assertNotNull(result);
        assertEquals(SUCCESS_STATUS, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithInvalidVoucherCodeSetsRejectedStatus() {
        Map<String, String> invalidPaymentData = new HashMap<>();
        invalidPaymentData.put("voucherCode", "INVALID");

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(this.order, VOUCHER_METHOD, invalidPaymentData);

        assertNotNull(result);
        assertEquals(REJECTED_STATUS, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithValidBankTransferKeepsPendingStatus() {
        Map<String, String> bankTransferData = new HashMap<>();
        bankTransferData.put("bankName", "BCA");
        bankTransferData.put("referenceCode", "TRF-001");

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(this.order, BANK_TRANSFER_METHOD, bankTransferData);

        assertNotNull(result);
        assertEquals(PENDING_STATUS, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithEmptyBankTransferDataSetsRejectedStatus() {
        Map<String, String> invalidBankTransferData = new HashMap<>();
        invalidBankTransferData.put("bankName", "");
        invalidBankTransferData.put("referenceCode", "TRF-001");

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(this.order, BANK_TRANSFER_METHOD, invalidBankTransferData);

        assertNotNull(result);
        assertEquals(REJECTED_STATUS, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithNullBankTransferDataSetsRejectedStatus() {
        Map<String, String> invalidBankTransferData = new HashMap<>();
        invalidBankTransferData.put("bankName", "BCA");
        invalidBankTransferData.put("referenceCode", null);

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(this.order, BANK_TRANSFER_METHOD, invalidBankTransferData);

        assertNotNull(result);
        assertEquals(REJECTED_STATUS, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccessUpdatesOrderStatus() {
        Payment payment = new Payment(PAYMENT_ID, this.order, VOUCHER_METHOD, PENDING_STATUS, this.paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, SUCCESS_STATUS);

        assertEquals(SUCCESS_STATUS, result.getStatus());
        assertEquals(SUCCESS_STATUS, this.order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusRejectedUpdatesOrderStatus() {
        Payment payment = new Payment(PAYMENT_ID, this.order, VOUCHER_METHOD, PENDING_STATUS, this.paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, REJECTED_STATUS);

        assertEquals(REJECTED_STATUS, result.getStatus());
        assertEquals(FAILED_STATUS, this.order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetPaymentIfIdFound() {
        Payment payment = new Payment(PAYMENT_ID, this.order, VOUCHER_METHOD, PENDING_STATUS, this.paymentData);
        doReturn(payment).when(paymentRepository).findById(PAYMENT_ID);

        Payment result = paymentService.getPayment(PAYMENT_ID);

        assertNotNull(result);
        assertEquals(PAYMENT_ID, result.getId());
    }

    @Test
    void testGetPaymentIfIdNotFound() {
        doReturn(null).when(paymentRepository).findById("missing-payment");

        Payment result = paymentService.getPayment("missing-payment");

        assertNull(result);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(PAYMENT_ID, this.order, VOUCHER_METHOD, PENDING_STATUS, this.paymentData));
        payments.add(new Payment("payment-2", this.order, BANK_TRANSFER_METHOD, SUCCESS_STATUS, this.paymentData));
        doReturn(payments).when(paymentRepository).findAllPayments();

        List<Payment> results = paymentService.getAllPayments();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(payment -> payment.getId().equals(PAYMENT_ID)));
        assertTrue(results.stream().anyMatch(payment -> payment.getId().equals("payment-2")));
    }
}
