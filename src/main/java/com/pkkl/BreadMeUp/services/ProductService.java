package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.Product;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.List;

public interface ProductService {

    Product getById(int id);

    List<Product> getAll();

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    void delete(int id, Principal principal);

    @PreAuthorize("hasRole('ROLE_MANAGER') && @methodSecurityExpression.isThisBakeryManager(principal, #product.bakery.id)")
    Product update(Product product);

    @PreAuthorize("hasRole('ROLE_MANAGER') && @methodSecurityExpression.isThisBakeryManager(principal, #product.bakery.id)")
    Product add(Product product);

    List<Product> getByBakery(int bakeryId);

    List<Product> getByCategory(int categoryId);

    List<Product> getByCategoryAndBakery(int categoryId, int bakeryId);
}
