package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.*;
import com.pkkl.BreadMeUp.repositories.ProductAvailabilityRepository;
import com.pkkl.BreadMeUp.repositories.ProductRepository;
import com.pkkl.BreadMeUp.security.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final BakeryService bakeryService;

    private final CategoryService categoryService;

    private final ProductTypeService productTypeService;

    private final ProductAvailabilityRepository productAvailabilityRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BakeryService bakeryService,
                              CategoryService categoryService, ProductTypeService productTypeService,
                              ProductAvailabilityRepository productAvailabilityRepository) {
        this.productRepository = productRepository;
        this.bakeryService = bakeryService;
        this.categoryService = categoryService;
        this.productTypeService = productTypeService;
        this.productAvailabilityRepository = productAvailabilityRepository;
    }

    @Override
    public Product getById(final int id) {
        try {
            return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product doesn't exist \\U+1F635"));
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getAll() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(final int id, Principal principal) {
        try {
            this.productRepository.findById(id)
                    .ifPresent(
                            (product) -> {
                                if (this.getBakeryIdFromPrincipal(principal) != product.getBakery().getId()) {
                                    throw new AccessDeniedException("User is not this bakery manager");
                                }
                                productRepository.deleteById(id);
                            }
                    );
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private int getBakeryIdFromPrincipal(Principal principal) {
        return this.getAuthUserDetails(principal).getUser().getBakery().getId();
    }

    private AuthUserDetails getAuthUserDetails(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        return (AuthUserDetails) token.getPrincipal();
    }

    @Transactional
    @Override
    public Product update(final Product product) {
        try {
            this.setBakeryCategoryProductTypeForProduct(product);

            List<ProductAvailability> productAvailabilities = this.productAvailabilityRepository.findAllByProduct(product);
            productAvailabilities.forEach(pa -> pa.setProduct(product));
            product.setProductAvailability(productAvailabilities);

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

    @Transactional
    @Override
    public Product add(final Product product) {
        try {
            this.setBakeryCategoryProductTypeForProduct(product);

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

    private void setBakeryCategoryProductTypeForProduct(Product product) {
        Bakery bakery = this.bakeryService.getById(product.getBakery().getId());
        Category category = this.categoryService.getById(product.getCategory().getId());
        ProductType productType = this.productTypeService.getById(product.getProductType().getId());


        product.setBakery(bakery);
        product.setCategory(category);
        product.setProductType(productType);
    }

    @Override
    public List<Product> getByBakery(final int bakeryId) {
        try {
            return productRepository.findAll().stream()
                    .filter(p -> containsBakery(p, bakeryId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getByCategory(final int categoryId) {
        try {
            return productRepository.findAll().stream()
                    .filter(p -> containsCategory(p, categoryId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private boolean containsBakery(final Product product, final int bakeryId) {
        return product.getBakery().getId() == bakeryId;
    }

    private boolean containsCategory(final Product product, final int categoryId) {
        return product.getCategory().getId() == categoryId;
    }
}
