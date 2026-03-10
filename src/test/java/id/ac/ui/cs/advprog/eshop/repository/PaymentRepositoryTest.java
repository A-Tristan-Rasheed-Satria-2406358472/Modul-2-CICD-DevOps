package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payment = new Payment(
                "payment-1",
                "Voucher Code",
                "SUCCESS",
                paymentData
        );
    }

    @Test
    void testSaveCreate() {
        Payment result = paymentRepository.save(payment);
        Payment found = paymentRepository.findById(payment.getId());

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), found.getId());
        assertEquals(payment.getMethod(), found.getMethod());
        assertEquals(payment.getStatus(), found.getStatus());
    }

    @Test
    void testFindByIdReturnsNullWhenNotFound() {
        Payment found = paymentRepository.findById("unknown-id");

        assertNull(found);
    }

    @Test
    void testSaveUpdateDoesNotCreateDuplicatePayment() {
        paymentRepository.save(payment);

        Map<String, String> updatedPaymentData = new HashMap<>();
        updatedPaymentData.put("voucherCode", "ESHOP9999ABC5678");
        Payment updatedPayment = new Payment(
                "payment-1",
                "Voucher Code",
                "REJECTED",
                updatedPaymentData
        );

        Payment result = paymentRepository.save(updatedPayment);
        List<Payment> payments = paymentRepository.findAllPayments();

        assertEquals("payment-1", result.getId());
        assertEquals(1, payments.size());
        assertEquals("REJECTED", payments.get(0).getStatus());
        assertEquals("ESHOP9999ABC5678", payments.get(0).getPaymentData().get("voucherCode"));
    }

    @Test
    void testFindAllPaymentsReturnsSavedPayments() {
        paymentRepository.save(payment);

        Map<String, String> bankTransferData = new HashMap<>();
        bankTransferData.put("bankName", "BCA");
        bankTransferData.put("referenceCode", "TRF-001");

        Payment secondPayment = new Payment(
                "payment-2",
                "Bank Transfer",
                "PENDING",
                bankTransferData
        );
        paymentRepository.save(secondPayment);

        List<Payment> payments = paymentRepository.findAllPayments();

        assertEquals(2, payments.size());
        assertEquals("payment-1", payments.get(0).getId());
        assertEquals("payment-2", payments.get(1).getId());
    }

    @Test
    void testFindAllPaymentsReturnsEmptyListWhenNoData() {
        List<Payment> payments = paymentRepository.findAllPayments();

        assertTrue(payments.isEmpty());
    }
}
