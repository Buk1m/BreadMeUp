package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.ProductType;
import com.pkkl.BreadMeUp.repositories.ProductTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Slf4j

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public ProductType getById(int id) {
        try {
            return productTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Product type doesn't exist"));
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<ProductType> getAll() {
        try {
            return productTypeRepository.findAll();
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            productTypeRepository.deleteById(id);
        } catch (Exception e){
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public ProductType update(final ProductType productType) {
        return saveOrUpdate(productType);
    }

    @Override
    public ProductType add(final ProductType productType) {
        return saveOrUpdate(productType);
    }

    private ProductType saveOrUpdate(final ProductType productType){
        try {
            return productTypeRepository.save(productType);
        } catch (ConstraintViolationException e) {
            throw new ConstraintException(e.getConstraintViolations().toString(), e);
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new ConstraintException(e.getMessage(), e);
            }
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
