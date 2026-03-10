package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private static final String REDIRECT_PAYMENT_ADMIN_LIST = "redirect:/payment/admin/list";
    private static final String PAYMENT_ATTRIBUTE = "payment";

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailForm() {
        return "PaymentDetail";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetailPage(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute(PAYMENT_ATTRIBUTE, payment);
        return "PaymentDetail";
    }

    @GetMapping("/admin/list")
    public String paymentAdminListPage(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "PaymentList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String paymentAdminDetailPage(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            return REDIRECT_PAYMENT_ADMIN_LIST;
        }

        model.addAttribute(PAYMENT_ATTRIBUTE, payment);
        return "PaymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(
            @PathVariable String paymentId,
            @RequestParam("status") String status,
            Model model
    ) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            return REDIRECT_PAYMENT_ADMIN_LIST;
        }

        Payment updatedPayment = paymentService.setStatus(payment, status);
        model.addAttribute(PAYMENT_ATTRIBUTE, updatedPayment);
        return "PaymentAdminDetail";
    }
}
