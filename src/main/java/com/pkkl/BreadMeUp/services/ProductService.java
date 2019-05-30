package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.Bakery;
import com.pkkl.BreadMeUp.model.Category;
import com.pkkl.BreadMeUp.model.Product;

import java.util.List;

public interface ProductService {
    Product getById(int id);
    List<Product> getAll();
    void delete(int id);
    Product update(Product product);
    Product add(Product product);
    List<Product> getByBakery(Bakery bakery);
    List<Product> getByCategory(Category category);
    List<Product> getByCategoryAndBakery(Category category, Bakery bakery);
}
