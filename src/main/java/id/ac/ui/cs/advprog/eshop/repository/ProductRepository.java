package id.ac.ui.cs.advprog.eshop.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.eshop.model.Product;

@Repository

public class ProductRepository {

    private List<Product> productData = new ArrayList<>();
    private int nextId = 1;

    public Product create(Product product) {
        product.setProductId(String.valueOf(nextId++));
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String id) {
        for (Product p : productData) {
            if (p.getProductId() != null && p.getProductId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Product update(Product product) {
        Product existing = findById(product.getProductId());
        if (existing != null) {
            existing.setProductName(product.getProductName());
            existing.setProductQuantity(product.getProductQuantity());
        }
        return existing;
    }

    public boolean deleteById(String id) {
        Iterator<Product> it = productData.iterator();
        while (it.hasNext()) {
            Product p = it.next();
            if (p.getProductId() != null && p.getProductId().equals(id)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

}
