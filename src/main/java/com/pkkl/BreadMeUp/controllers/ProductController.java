package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.ProductDto;
import com.pkkl.BreadMeUp.model.Product;
import com.pkkl.BreadMeUp.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getById(@PathVariable(name = "id") int id) {
        return mapProductToProductDto(
                productService.getById(id)
        );
    }

    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAll() {
        return mapProductListToProductDtoList(
                productService.getAll()
        );
    }

    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id) {
        productService.delete(id);
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductDto update(@PathVariable(name = "id") int id, @Valid @RequestBody final ProductDto productDto) {
        if (id != productDto.getId()) {
            //TODO
        }
        return mapProductToProductDto(
                productService.update(
                        mapProductDtoToProduct(productDto)
                )
        );
    }

    @PostMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto add( @Valid @RequestBody final ProductDto productDto) {
        return mapProductToProductDto(
                productService.add(
                        mapProductDtoToProduct(productDto)
                )
        );
    }

    @GetMapping(
            value = "/{bakeryId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getByBakery(@PathVariable(name = "bakeryId") int bakeryId){
        return mapProductListToProductDtoList(
                productService.getByBakery(bakeryId)
        );
    }


    @GetMapping(
            value = "/{categoryId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getByCategory(@PathVariable(name = "categoryId") int categoryId){
        return mapProductListToProductDtoList(
                productService.getByCategory(categoryId)
        );
    }

    @GetMapping(
            value = "/{bakeryId}/{categoryId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getByCategory(@PathVariable(name = "bakeryId") int bakeryId, @PathVariable(name = "categoryId") int categoryId){
        return mapProductListToProductDtoList(
                productService.getByCategoryAndBakery(categoryId, bakeryId)
        );
    }


    private Product mapProductDtoToProduct(final ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    private ProductDto mapProductToProductDto(final Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    private List<ProductDto> mapProductListToProductDtoList(final List<Product> products){
        return products.stream()
                .map(this::mapProductToProductDto)
                .collect(Collectors.toList());
    }
}
