package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.Product;
import com.pkkl.BreadMeUp.model.ProductAvailability;
import com.pkkl.BreadMeUp.repositories.ProductAvailabilityRepository;
import com.pkkl.BreadMeUp.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Service
public class ProductAvailabilityServiceImpl implements ProductAvailabilityService {

    private final ProductAvailabilityRepository productAvailabilityRepository;

    private final ProductRepository productRepository;

    @Autowired
    public ProductAvailabilityServiceImpl(ProductAvailabilityRepository productAvailabilityRepository,
                                          ProductRepository productRepository) {
        this.productAvailabilityRepository = productAvailabilityRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductAvailability getById(int id) {
        try {
            return productAvailabilityRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("ProductAvailability doesn't exist"));
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public ProductAvailability update(final ProductAvailability productAvailability) {
        return saveOrUpdate(productAvailability);
    }

    @Override
    public ProductAvailability add(final ProductAvailability productAvailability) {
        return saveOrUpdate(productAvailability);
    }

    @Override
    public ProductAvailability getByDateAndProduct(final LocalDate date, final int productId) {
        try {
            return productAvailabilityRepository.findAll().stream()
                    .filter(p -> contains(p, date))
                    .filter(p -> contains(p, productId))
                    .findAny().orElseGet(
                            () -> this.createProductAvailabilityFromProduct(date, productId)
                    );
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<ProductAvailability> getAllByProduct(int productId) {
        try {
            return productAvailabilityRepository.findAll().stream()
                    .filter(p -> contains(p, productId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private ProductAvailability saveOrUpdate(final ProductAvailability productAvailability) {
        try {
            return productAvailabilityRepository.save(productAvailability);
        } catch (ConstraintViolationException e) {
            throw new ConstraintException(e.getConstraintViolations().toString(), e);
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new ConstraintException(e.getMessage(), e);
            }
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private boolean contains(final ProductAvailability productAvailability, final int productId) {
        return productAvailability.getProduct().getId() == productId;
    }

    private boolean contains(final ProductAvailability productAvailability, final LocalDate date) {
        return productAvailability.getDate().equals(date);
    }

    private ProductAvailability createProductAvailabilityFromProduct(final LocalDate localDate, final int productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new DatabaseException("Product does not exist with id" + productId));

        return ProductAvailability.builder()
                .date(localDate)
                .limit(product.getLimit())
                .orderedNumber(0)
                .product(product)
                .build();
    }
}
