package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.ProductDetailsDto;
import com.pkkl.BreadMeUp.dtos.ProductDto;
import com.pkkl.BreadMeUp.model.Product;
import com.pkkl.BreadMeUp.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    private final ProductService productService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(
            value = "/products/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailsDto getById(@PathVariable(name = "id") int id) {
        return mapProductToProductDetailsDto(
                productService.getById(id)
        );
    }

    @GetMapping(
            value = "/products",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDetailsDto> getAll() {
        return mapProductListToProductDetailsDtoList(
                productService.getAll()
        );
    }

    @DeleteMapping(
            value = "/manager/products/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id, Principal principal) {
        productService.delete(id, principal);
    }

    @PutMapping(
            value = "/manager/products/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailsDto update(@PathVariable(name = "id") int id, @Valid @RequestBody final ProductDto productDto) {
        productDto.setId(id);
        return mapProductToProductDetailsDto(
                productService.update(
                        mapProductDtoToProduct(productDto)
                )
        );
    }

    @PostMapping(
            value = "/manager/products",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetailsDto add(@Valid @RequestBody final ProductDto productDto) {
        return mapProductToProductDetailsDto(
                productService.add(
                        mapProductDtoToProduct(productDto)
                )
        );
    }

    @GetMapping(
            value = "/products/bakeries/{bakeryId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDetailsDto> getByBakery(@PathVariable(name = "bakeryId") int bakeryId) {
        return mapProductListToProductDetailsDtoList(
                productService.getByBakery(bakeryId)
        );
    }


    @GetMapping(
            value = "/products/categories/{categoryId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDetailsDto> getByCategory(@PathVariable(name = "categoryId") int categoryId) {
        return mapProductListToProductDetailsDtoList(
                productService.getByCategory(categoryId)
        );
    }

    @GetMapping(
            value = "/products/bakeries/{bakeryId}/categories/{categoryId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDetailsDto> getByCategoryAndBakery(@PathVariable(name = "bakeryId") int bakeryId, @PathVariable(name = "categoryId") int categoryId) {
        return mapProductListToProductDetailsDtoList(
                productService.getByCategoryAndBakery(categoryId, bakeryId)
        );
    }


    private Product mapProductDtoToProduct(final ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    private ProductDetailsDto mapProductToProductDetailsDto(final Product product) {
        return modelMapper.map(product, ProductDetailsDto.class);
    }

    private List<ProductDetailsDto> mapProductListToProductDetailsDtoList(final List<Product> products) {
        return products.stream()
                .map(this::mapProductToProductDetailsDto)
                .collect(Collectors.toList());
    }
}
