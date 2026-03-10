package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String PAYMENT_STATUS_PENDING = "PENDING";
    private static final String PAYMENT_STATUS_SUCCESS = "SUCCESS";
    private static final String PAYMENT_STATUS_REJECTED = "REJECTED";
    private static final String ORDER_STATUS_SUCCESS = "SUCCESS";
    private static final String ORDER_STATUS_FAILED = "FAILED";

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                order,
                method,
                PAYMENT_STATUS_PENDING,
                paymentData
        );
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        syncOrderStatus(payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllPayments();
    }

    private void syncOrderStatus(Payment payment) {
        if (PAYMENT_STATUS_SUCCESS.equals(payment.getStatus())) {
            payment.getOrder().setStatus(ORDER_STATUS_SUCCESS);
        } else if (PAYMENT_STATUS_REJECTED.equals(payment.getStatus())) {
            payment.getOrder().setStatus(ORDER_STATUS_FAILED);
        }
    }
}
