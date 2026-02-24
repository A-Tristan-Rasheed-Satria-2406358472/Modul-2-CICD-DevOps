package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

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
class ProductRepositoryTest {

  @InjectMocks
  ProductRepository productRepository;

  private Product newProduct(String name, int quantity) {
    Product product = new Product();
    product.setProductName(name);
    product.setProductQuantity(quantity);
    return product;
  }

  @Test
  void testCreateAndFind() {
    Product product = new Product();
    product.setProductId("abc-123");
    product.setProductName("Sampo Cap Bambang");
    product.setProductQuantity(100);
    productRepository.create(product);

    Iterator<Product> productIterator = productRepository.findAll();
    assertTrue(productIterator.hasNext());
    Product savedProduct = productIterator.next();
    assertEquals(product.getProductId(), savedProduct.getProductId());
    assertEquals(product.getProductName(), savedProduct.getProductName());
    assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
  }

  @Test
  void testFindAllIfEmpty() {
    Iterator<Product> productIterator = productRepository.findAll();
    assertFalse(productIterator.hasNext());
  }

  @Test
  void testFindAllIfMoreThanOneProduct() {
    Product product1 = new Product();
    product1.setProductId("abc-123");
    product1.setProductName("Sampo Cap Bambang");
    product1.setProductQuantity(100);
    productRepository.create(product1);

    Product product2 = new Product();
    product2.setProductId("abc-124");
    product2.setProductName("Sampo Cap Usep");
    product2.setProductQuantity(50);
    productRepository.create(product2);

    Iterator<Product> productIterator = productRepository.findAll();
    assertTrue(productIterator.hasNext());
    Product savedProduct = productIterator.next();
    assertEquals(product1.getProductId(), savedProduct.getProductId());
    savedProduct = productIterator.next();
    assertEquals(product2.getProductId(), savedProduct.getProductId());
    assertFalse(productIterator.hasNext());
  }

  @Test
  void testFindByIdReturnsProductWhenFound() {
    Product created = productRepository.create(newProduct("Keyboard", 5));

    Product found = productRepository.findById(created.getProductId());

    assertNotNull(found);
    assertEquals(created.getProductId(), found.getProductId());
  }

  @Test
  void testFindByIdReturnsNullWhenNotFound() {
    productRepository.create(newProduct("Mouse", 7));

    Product found = productRepository.findById("999");

    assertNull(found);
  }

  @Test
  void testFindByIdSkipsProductWithNullId() {
    Product nullIdProduct = productRepository.create(newProduct("Broken Product", 1));
    nullIdProduct.setProductId(null);

    Product found = productRepository.findById("1");

    assertNull(found);
  }

  @Test
  void testUpdateReturnsUpdatedProductWhenFound() {
    Product created = productRepository.create(newProduct("Old Name", 2));

    Product updateRequest = new Product();
    updateRequest.setProductId(created.getProductId());
    updateRequest.setProductName("New Name");
    updateRequest.setProductQuantity(10);

    Product updated = productRepository.update(updateRequest);

    assertNotNull(updated);
    assertEquals("New Name", updated.getProductName());
    assertEquals(10, updated.getProductQuantity());
  }

  @Test
  void testUpdateReturnsNullWhenProductDoesNotExist() {
    Product updateRequest = new Product();
    updateRequest.setProductId("999");
    updateRequest.setProductName("Nope");
    updateRequest.setProductQuantity(1);

    Product updated = productRepository.update(updateRequest);

    assertNull(updated);
  }

  @Test
  void testDeleteByIdReturnsTrueWhenFound() {
    Product created = productRepository.create(newProduct("To Delete", 3));

    boolean deleted = productRepository.deleteById(created.getProductId());

    assertTrue(deleted);
    assertNull(productRepository.findById(created.getProductId()));
  }

  @Test
  void testDeleteByIdReturnsFalseWhenNotFound() {
    productRepository.create(newProduct("Existing", 3));

    boolean deleted = productRepository.deleteById("999");

    assertFalse(deleted);
  }

  @Test
  void testDeleteByIdSkipsProductWithNullId() {
    Product nullIdProduct = productRepository.create(newProduct("No Id Product", 1));
    nullIdProduct.setProductId(null);

    boolean deleted = productRepository.deleteById("1");

    assertFalse(deleted);
  }
}
