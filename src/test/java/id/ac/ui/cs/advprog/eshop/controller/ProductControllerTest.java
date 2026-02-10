package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
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
                .param("productQuantity", "7")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        ArgumentCaptor<Product> cap = ArgumentCaptor.forClass(Product.class);
        verify(service).update(cap.capture());
        Product sent = cap.getValue();
        assert sent.getProductId().equals("42");
        assert sent.getProductName().equals("Edited");
        assert sent.getProductQuantity() == 7;
    }

    @Test
    void deleteProduct_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(get("/product/delete/5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service).deleteById("5");
    }
}
