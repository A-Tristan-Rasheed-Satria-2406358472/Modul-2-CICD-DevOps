package id.ac.ui.cs.advprog.eshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Product;

@ExtendWith(MockitoExtension.class)
class ProductEditDeleteTest {

    @InjectMocks
    ProductRepository productRepository;

    private Product makeProduct(String name, int qty) {
        Product p = new Product();
        p.setProductName(name);
        p.setProductQuantity(qty);
        return p;
    }

    @Test
    void testUpdateExistingProduct() {
        Product product = new Product();
        product.setProductName("Old Name");
        product.setProductQuantity(10);
        Product saved = productRepository.create(product);

        Product toUpdate = new Product();
        toUpdate.setProductId(saved.getProductId());
        toUpdate.setProductName("New Name");
        toUpdate.setProductQuantity(20);

        Product updated = productRepository.update(toUpdate);
        assertNotNull(updated);
        assertEquals(saved.getProductId(), updated.getProductId());
        assertEquals("New Name", updated.getProductName());
        assertEquals(20, updated.getProductQuantity());

        Product fetched = productRepository.findById(saved.getProductId());
        assertNotNull(fetched);
        assertEquals("New Name", fetched.getProductName());
        assertEquals(20, fetched.getProductQuantity());
    }

    @Test
    void testUpdateNonExistingProductReturnsNull() {
        Product toUpdate = new Product();
        toUpdate.setProductId("non-existent-id");
        toUpdate.setProductName("Name");
        toUpdate.setProductQuantity(5);

        Product updated = productRepository.update(toUpdate);
        assertNull(updated);
    }

    @Test
    void testDeleteExistingProduct() {
        Product toDelete = makeProduct("ToDelete", 5);
        Product toKeep = makeProduct("KeepMe", 3);
        Product savedDelete = productRepository.create(toDelete);
        Product savedKeep = productRepository.create(toKeep);

        boolean deleted = productRepository.deleteById(savedDelete.getProductId());
        assertTrue(deleted);
        assertNull(productRepository.findById(savedDelete.getProductId()));
        assertNotNull(productRepository.findById(savedKeep.getProductId()));
    }

    @Test
    void testDeleteNonExistingProductReturnsFalse() {
        boolean deleted = productRepository.deleteById("no-such-id");
        assertFalse(deleted);
    }

    @Test
    void testUpdateAllowsNegativeQuantity() {
        Product p = makeProduct("Initial", 5);
        Product saved = productRepository.create(p);

        Product toUpdate = new Product();
        toUpdate.setProductId(saved.getProductId());
        toUpdate.setProductName("Initial (Adjusted)");
        toUpdate.setProductQuantity(-10);

        Product updated = productRepository.update(toUpdate);
        assertNotNull(updated);
        assertEquals(-10, updated.getProductQuantity());

        Product fetched = productRepository.findById(saved.getProductId());
        assertNotNull(fetched);
        assertEquals(-10, fetched.getProductQuantity());
    }
}
