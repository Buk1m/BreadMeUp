package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.Bakery;
import com.pkkl.BreadMeUp.model.Category;
import com.pkkl.BreadMeUp.model.Product;
import com.pkkl.BreadMeUp.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getById(final int id) {
        try {
            return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product doesn't exist \\U+1F635"));
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getAll() {
        try {
            return productRepository.findAll();
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(final int id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public Product update(final Product product) {
        return saveOrUpdate(product);
    }

    @Override
    public Product add(final Product product) {
        return saveOrUpdate(product);
    }

    @Override
    public List<Product> getByBakery(final int bakeryId) {
        try {
            return productRepository.findAll().stream()
                    .filter(p -> containsBakery(p, bakeryId))
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getByCategory(final int categoryId) {
        try {
            return productRepository.findAll().stream()
                    .filter(p -> containsCategory(p, categoryId))
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getByCategoryAndBakery(final int categoryId, final int bakeryId) {
        try {
            return productRepository.findAll().stream()
                    .filter(p -> containsBakery(p, bakeryId))
                    .filter(p -> containsCategory(p, categoryId))
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private Product saveOrUpdate(final Product product){
        try {
            return productRepository.save(product);
        } catch (ConstraintViolationException e) {
            throw new ConstraintException(e.getConstraintViolations().toString(), e);
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new ConstraintException(e.getMessage(), e);
            }
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private boolean containsBakery(final Product product, final int bakeryId){
        return product.getBakery().getId() == bakeryId;
    }

    private boolean containsCategory(final Product product, final int categoryId){
        return product.getCategory().getId() == categoryId;
    }
}
