package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.ProductType;

import java.util.List;

public interface ProductTypeService {
    ProductType getById(int id);
    List<ProductType> getAll();
    void delete(int id);
    ProductType update(final ProductType productType);
    ProductType add(final ProductType productType);
}
