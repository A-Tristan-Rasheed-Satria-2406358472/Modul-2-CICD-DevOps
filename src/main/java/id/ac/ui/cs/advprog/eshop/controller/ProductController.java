package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  private static final String REDIRECT_PRODUCT_LIST = "redirect:/product/list";

  @GetMapping("/create")
  public String createProductPage(Model model) {
    Product product = new Product();
    model.addAttribute("product", product);
    return "CreateProduct";
  }

  @PostMapping("/create")
  public String createProductPost(@ModelAttribute Product product) {
    service.create(product);
    return REDIRECT_PRODUCT_LIST;
  }

  @GetMapping("/list")
  public String productListPage(Model model) {
    List<Product> allProducts = service.findAll();
    model.addAttribute("products", allProducts);
    return "ProductList";
  }

  @GetMapping("/edit/{productId}")
  public String editProductPage(@PathVariable String productId, Model model) {
    Product product = service.findById(productId);
    if (product == null) {
      return REDIRECT_PRODUCT_LIST;
    }
    model.addAttribute("product", product);
    return "EditProduct";
  }

  @PostMapping("/edit")
  public String editProductPost(@ModelAttribute Product product) {
    service.update(product);
    return REDIRECT_PRODUCT_LIST;
  }

  @PostMapping("/delete")
  public String deleteProduct(@RequestParam("productId") String productId) {
    service.deleteById(productId);
    return REDIRECT_PRODUCT_LIST;
  }

  @GetMapping("/delete/{productId}")
  public String deleteProductGet(@PathVariable String productId) {
    service.deleteById(productId);
    return REDIRECT_PRODUCT_LIST;
  }
}