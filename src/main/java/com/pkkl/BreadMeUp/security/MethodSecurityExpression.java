package com.pkkl.BreadMeUp.security;

import com.pkkl.BreadMeUp.model.Product;
import com.pkkl.BreadMeUp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class MethodSecurityExpression {

    private final ProductService productService;

    @Autowired
    public MethodSecurityExpression(ProductService productService) {
        this.productService = productService;
    }

    public boolean isThisProductManager(UserDetails principal, int productId) {
        AuthUserDetails authUserDetails = (AuthUserDetails) principal;

        Product product = this.productService.getById(productId);

        if (authUserDetails.getUser().getBakery() != null) {
            return product.getBakery().getId() == authUserDetails.getUser().getBakery().getId();
        } else {
            return false;
        }
    }

    public boolean isThisBakeryManager(UserDetails principal, int bakeryId) {
        AuthUserDetails authUserDetails = (AuthUserDetails) principal;

        if (authUserDetails.getUser().getBakery() != null) {
            return bakeryId == authUserDetails.getUser().getBakery().getId();
        } else {
            return false;
        }
    }
}
