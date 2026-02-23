package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService service;

  @Test
  void createProductPage_returnsCreateViewAndAddsProductModel() throws Exception {
    mockMvc.perform(get("/product/create"))
        .andExpect(status().isOk())
        .andExpect(view().name("CreateProduct"))
        .andExpect(model().attributeExists("product"));
  }

  @Test
  void createProductPost_callsCreateAndRedirects() throws Exception {
    mockMvc.perform(post("/product/create")
        .param("productId", "123")
        .param("productName", "Mouse")
        .param("productQuantity", "4"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/product/list"));

    ArgumentCaptor<Product> cap = ArgumentCaptor.forClass(Product.class);
    verify(service).create(cap.capture());
    Product sent = cap.getValue();
    assertEquals("123", sent.getProductId());
    assertEquals("Mouse", sent.getProductName());
    assertEquals(4, sent.getProductQuantity());
  }

  @Test
  void productListPage_returnsListViewAndProductsModel() throws Exception {
    Product product = new Product();
    product.setProductId("P-1");
    product.setProductName("Keyboard");
    product.setProductQuantity(2);
    when(service.findAll()).thenReturn(List.of(product));

    mockMvc.perform(get("/product/list"))
        .andExpect(status().isOk())
        .andExpect(view().name("ProductList"))
        .andExpect(model().attributeExists("products"));

    verify(service).findAll();
  }

  @Test
  void editProductPage_whenProductExists_returnsEditViewWithModel() throws Exception {
    Product p = new Product();
    p.setProductId("1");
    p.setProductName("A");
    when(service.findById("1")).thenReturn(p);

    mockMvc.perform(get("/product/edit/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("EditProduct"))
        .andExpect(model().attributeExists("product"));

    verify(service).findById("1");
  }

  @Test
  void editProductPage_whenProductNotFound_redirectsToList() throws Exception {
    when(service.findById("9")).thenReturn(null);

    mockMvc.perform(get("/product/edit/9"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/product/list"));

    verify(service).findById("9");
  }

  @Test
  void editProductPost_callsUpdate_andRedirects() throws Exception {
    mockMvc.perform(post("/product/edit")
        .param("productId", "42")
        .param("productName", "Edited")
        .param("productQuantity", "7"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/product/list"));

    ArgumentCaptor<Product> cap = ArgumentCaptor.forClass(Product.class);
    verify(service).update(cap.capture());
    Product sent = cap.getValue();
    assertEquals("42", sent.getProductId());
    assertEquals("Edited", sent.getProductName());
    assertEquals(7, sent.getProductQuantity());
  }

  @Test
  void deleteProduct_callsServiceAndRedirects() throws Exception {
    mockMvc.perform(get("/product/delete/5"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/product/list"));

    verify(service).deleteById("5");
  }
}
