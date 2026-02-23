package id.ac.ui.cs.advprog.eshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

  private static final String REDIRECT_PRODUCT_LIST = "redirect:/product/list";

  @Autowired
  private ProductService service;

  @GetMapping("/create")
  public String createProductPage(Model model) {
    Product product = new Product();
    model.addAttribute("product", product);
    return "CreateProduct";
  }

  @PostMapping("/create")
  public String createProductPost(@ModelAttribute Product product, Model model) {
    service.create(product);
    return REDIRECT_PRODUCT_LIST;
  }

  @GetMapping("/list")
  public String productListPage(Model model) {
    List<Product> allProducts = service.findAll();
    model.addAttribute("products", allProducts);
    return "ProductList";
  }

  @GetMapping("/edit/{id}")
  public String editProductPage(@PathVariable("id") String id, Model model) {
    Product product = service.findById(id);
    if (product == null) {
      return REDIRECT_PRODUCT_LIST;
    }
    model.addAttribute("product", product);
    return "EditProduct";
  }

  @PostMapping("/edit")
  public String editProductPost(@ModelAttribute Product product, Model model) {
    service.update(product);
    return REDIRECT_PRODUCT_LIST;
  }

  @GetMapping("/delete/{id}")
  public String deleteProduct(@PathVariable("id") String id, Model model) {
    service.deleteById(id);
    return REDIRECT_PRODUCT_LIST;
  }
}
