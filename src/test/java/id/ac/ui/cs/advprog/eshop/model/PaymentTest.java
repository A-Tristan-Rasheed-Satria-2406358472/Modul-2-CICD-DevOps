package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentTest {

    private Map<String, String> paymentData;
    private Payment payment;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP1234ABC5678");

        this.payment = new Payment(
                "payment-1",
                "Voucher Code",
                "SUCCESS",
                this.paymentData
        );
    }

    @Test
    void testGetPaymentId() {
        assertEquals("payment-1", payment.getId());
    }

    @Test
    void testGetPaymentMethod() {
        assertEquals("Voucher Code", payment.getMethod());
    }

    @Test
    void testGetPaymentStatus() {
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testGetPaymentData() {
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
        assertEquals(1, payment.getPaymentData().size());
    }
}
