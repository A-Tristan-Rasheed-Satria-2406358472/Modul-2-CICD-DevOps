package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;

import java.util.Iterator;

public interface ProductDataStore {

  Product create(Product product);

  Iterator<Product> findAll();

  Product findById(String id);

  Product update(Product product);

  boolean deleteById(String id);
}