package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.exceptions.NotFoundException;
import com.pkkl.BreadMeUp.model.Product;
import com.pkkl.BreadMeUp.model.ProductAvailability;
import com.pkkl.BreadMeUp.repositories.ProductAvailabilityRepository;
import com.pkkl.BreadMeUp.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return productAvailabilityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("ProductAvailability doesn't exist"));
    }

    @Transactional
    @Override
    public ProductAvailability update(final ProductAvailability productAvailability) {
        try {
            Product product = this.productRepository.findById(productAvailability.getProduct().getId())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Product does not exist");
                    });
            productAvailability.setProduct(product);
            product.setProductAvailability(
                    product.getProductAvailability().stream()
                            .filter((pa) -> pa.getId() != productAvailability.getId())
                            .collect(Collectors.toList()));
            product.getProductAvailability().add(productAvailability);
            this.productRepository.save(product);
            return productAvailability;
        } catch (ConstraintViolationException e) {
            log.error(e.getMessage(), e);
            throw new ConstraintException(e.getConstraintViolations().toString(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new ConstraintException(e.getMessage(), e);
            }
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public ProductAvailability add(final ProductAvailability productAvailability) {
        try {
            Product product = this.productRepository.findById(productAvailability.getProduct().getId())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Product does not exist");
                    });
            productAvailability.setProduct(product);
            ProductAvailability savedProductAvailability = this.productAvailabilityRepository.save(productAvailability);
            product.getProductAvailability().add(savedProductAvailability);
            this.productRepository.save(product);
            return savedProductAvailability;
        } catch (ConstraintViolationException e) {
            throw new ConstraintException(e.getConstraintViolations().toString(), e);
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                log.error(e.getMessage(), e);
                throw new ConstraintException(e.getMessage(), e);
            }
            throw new DatabaseException(e.getMessage(), e);
        }
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
            log.error(e.getMessage(), e);
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
