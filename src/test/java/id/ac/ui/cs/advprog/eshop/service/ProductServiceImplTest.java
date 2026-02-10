package id.ac.ui.cs.advprog.eshop.service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void create_delegatesToRepository_andReturnsProduct() {
        Product p = new Product();
        p.setProductName("X");

        when(productRepository.create(p)).thenReturn(p);

        Product r = service.create(p);

        assertSame(p, r);
        verify(productRepository).create(p);
    }

    @Test
    void findAll_convertsIteratorToList() {
        Product a = new Product();
        a.setProductId("1");
        Product b = new Product();
        b.setProductId("2");
        List<Product> list = Arrays.asList(a, b);
        Iterator<Product> it = list.iterator();

        when(productRepository.findAll()).thenReturn(it);

        List<Product> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getProductId());
        assertEquals("2", result.get(1).getProductId());
    }

    @Test
    void findById_delegates() {
        Product p = new Product();
        p.setProductId("10");
        when(productRepository.findById("10")).thenReturn(p);

        Product r = service.findById("10");
        assertSame(p, r);
        verify(productRepository).findById("10");
    }

    @Test
    void update_delegates() {
        Product p = new Product();
        p.setProductId("x");
        when(productRepository.update(p)).thenReturn(p);

        Product r = service.update(p);
        assertSame(p, r);
        verify(productRepository).update(p);
    }

    @Test
    void deleteById_delegates() {
        when(productRepository.deleteById("z")).thenReturn(true);

        boolean ok = service.deleteById("z");
        assertTrue(ok);
        verify(productRepository).deleteById("z");
    }
}
