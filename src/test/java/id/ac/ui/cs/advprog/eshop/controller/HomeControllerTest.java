package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTest {

  private final HomeController controller = new HomeController();

  @Test
  void homePage_returnsHomePageView() {
    assertEquals("HomePage", controller.homePage());
  }

  @Test
  void productRoot_redirectsToProductList() {
    assertEquals("redirect:/product/list", controller.productRoot());
  }
}
