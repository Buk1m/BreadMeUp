package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.Product;

import java.util.List;

public interface ProductService {
    Product getById(int id);
    List<Product> getAll();
    void delete(int id);
    Product update(Product product);
    Product add(Product product);
    List<Product> getByBakery(int bakeryId);
    List<Product> getByCategory(int categoryId);
    List<Product> getByCategoryAndBakery(int categoryId, int bakeryId);
}
