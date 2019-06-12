package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.ProductTypeDto;
import com.pkkl.BreadMeUp.model.ProductType;
import com.pkkl.BreadMeUp.services.ProductTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/admin/types")
@RestController
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProductTypeController(ProductTypeService productTypeService, ModelMapper modelMapper) {
        this.productTypeService = productTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ProductTypeDto getById(@PathVariable(name = "id") int id) {
        return this.mapProductTypeToProductTypeDto(
                this.productTypeService.getById(id)
        );
    }

    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<ProductTypeDto> getAll() {
        return this.mapProductTypeListToProductTypeDtoList(
                this.productTypeService.getAll()
        );
    }

    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id) {
        this.productTypeService.delete(id);
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductTypeDto update(@PathVariable(name = "id") int id, @Valid @RequestBody final ProductTypeDto productTypeDto) {
        productTypeDto.setId(id);
        return this.mapProductTypeToProductTypeDto(
                this.productTypeService.update(
                        this.mapProductTypeDtoToProductType(productTypeDto)
                )
        );
    }

    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTypeDto add(@Valid @RequestBody ProductTypeDto productTypeDto) {
        return this.mapProductTypeToProductTypeDto(
                this.productTypeService.add(
                        this.mapProductTypeDtoToProductType(productTypeDto)
                )
        );
    }

    private ProductType mapProductTypeDtoToProductType(final ProductTypeDto productTypeDto) {
        return modelMapper.map(productTypeDto, ProductType.class);
    }

    private ProductTypeDto mapProductTypeToProductTypeDto(final ProductType productType) {
        return modelMapper.map(productType, ProductTypeDto.class);
    }

    private List<ProductTypeDto> mapProductTypeListToProductTypeDtoList(final List<ProductType> productsTypes) {
        return productsTypes.stream()
                .map(this::mapProductTypeToProductTypeDto)
                .collect(Collectors.toList());
    }
}
