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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

@Controller
@RequestMapping("/order")
public class OrderController {

  private static final String REDIRECT_ORDER_HISTORY = "redirect:/order/history";
  private static final String AUTHOR_ATTRIBUTE = "author";

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
  public String orderHistoryPost(@RequestParam("author") String author, RedirectAttributes redirectAttributes) {
    redirectAttributes.addAttribute(AUTHOR_ATTRIBUTE, author);
    return "redirect:/order/history/result";
  }

  @GetMapping("/history/result")
  public String orderHistoryResult(@RequestParam(value = "author", required = false) String author, Model model) {
    if (author != null) {
      List<Order> orders = orderService.findAllByAuthor(author);
      model.addAttribute(AUTHOR_ATTRIBUTE, author);
      model.addAttribute("orders", orders);
    }
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
      RedirectAttributes redirectAttributes) {
    Order order = orderService.findById(orderId);
    if (order == null) {
      return REDIRECT_ORDER_HISTORY;
    }

    paymentData.remove("method");
    Payment payment = paymentService.addPayment(order, method, paymentData);
    redirectAttributes.addAttribute("paymentId", payment.getId());
    return "redirect:/order/pay-result";
  }

  @GetMapping("/pay-result")
  public String payOrderResult(@RequestParam String paymentId, Model model) {
    model.addAttribute("paymentId", paymentId);
    return "OrderPayResult";
  }
}
