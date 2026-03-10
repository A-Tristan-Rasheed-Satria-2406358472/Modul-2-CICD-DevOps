package id.ac.ui.cs.advprog.eshop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

@Controller
@RequestMapping("/order")
public class OrderController {

  private static final String REDIRECT_ORDER_HISTORY = "redirect:/order/history";

  private final OrderService orderService;
  private final PaymentService paymentService;

  public OrderController(OrderService orderService, PaymentService paymentService) {
    this.orderService = orderService;
    this.paymentService = paymentService;
  }

  @GetMapping("/create")
  public String createOrderPage() {
    return "CreateOrder";
  }

  @GetMapping("/history")
  public String orderHistoryPage() {
    return "OrderHistory";
  }

  @PostMapping("/history/search")
  public String orderHistoryPost(@RequestParam("author") String author, Model model) {
    List<Order> orders = orderService.findAllByAuthor(author);
    model.addAttribute("author", author);
    model.addAttribute("orders", orders);
    return "OrderHistoryList";
  }

  @GetMapping("/pay/{orderId}")
  public String payOrderPage(@PathVariable String orderId, Model model) {
    Order order = orderService.findById(orderId);
    if (order == null) {
      return REDIRECT_ORDER_HISTORY;
    }
    model.addAttribute("order", order);
    return "OrderPay";
  }

  @PostMapping("/pay/{orderId}/process")
  public String payOrderPost(
      @PathVariable String orderId,
      @RequestParam("method") String method,
      @RequestParam Map<String, String> paymentData,
      Model model) {
    Order order = orderService.findById(orderId);
    if (order == null) {
      return REDIRECT_ORDER_HISTORY;
    }

    paymentData.remove("method");
    Payment payment = paymentService.addPayment(order, method, paymentData);
    model.addAttribute("paymentId", payment.getId());
    return "OrderPayResult";
  }
}
