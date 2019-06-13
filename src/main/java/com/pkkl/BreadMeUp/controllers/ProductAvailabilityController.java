package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.ProductAvailabilityDto;
import com.pkkl.BreadMeUp.model.ProductAvailability;
import com.pkkl.BreadMeUp.services.ProductAvailabilityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/availabilities")
@RestController
public class ProductAvailabilityController {

    private final ProductAvailabilityService productAvailabilityService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProductAvailabilityController(ProductAvailabilityService productAvailabilityService, ModelMapper modelMapper) {
        this.productAvailabilityService = productAvailabilityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public ProductAvailabilityDto getById(@PathVariable(name = "id") int id) {
        return mapProductAvailabilityToProductAvailabilityDto(
                this.productAvailabilityService.getById(id)
        );
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductAvailabilityDto update(@PathVariable(name = "id") int id,
                                         @Valid @RequestBody final ProductAvailabilityDto productAvailabilityDto) {
        productAvailabilityDto.setId(id);
        return this.mapProductAvailabilityToProductAvailabilityDto(
                this.productAvailabilityService.update(
                        this.mapProductAvailabilityDtoToProductAvailability(productAvailabilityDto)
                )
        );
    }

    @PostMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductAvailabilityDto add(@Valid @RequestBody final ProductAvailabilityDto productAvailabilityDto) {
        return this.mapProductAvailabilityToProductAvailabilityDto(
                this.productAvailabilityService.add(
                        this.mapProductAvailabilityDtoToProductAvailability(productAvailabilityDto)
                )
        );
    }

    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<ProductAvailabilityDto> getByDateOrDateAndProduct(@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate localDate,
                                                                  @RequestParam(name = "productId", required = false) Integer productId) {
        List<ProductAvailability> productAvailabilities;
        if (productId == null) {
            productAvailabilities = this.productAvailabilityService.getByDate(localDate);
        } else {
            productAvailabilities = this.productAvailabilityService.getByDateAndProduct(localDate, productId);
        }

        return this.mapProductAvailabilityListToProductAvailabilityDtoList(productAvailabilities);
    }

    private ProductAvailability mapProductAvailabilityDtoToProductAvailability(final ProductAvailabilityDto productAvailabilityDto) {
        return this.modelMapper.map(productAvailabilityDto, ProductAvailability.class);
    }

    private ProductAvailabilityDto mapProductAvailabilityToProductAvailabilityDto(final ProductAvailability productAvailability) {
        return this.modelMapper.map(productAvailability, ProductAvailabilityDto.class);
    }

    private List<ProductAvailabilityDto> mapProductAvailabilityListToProductAvailabilityDtoList(
            final List<ProductAvailability> productAvailabilities) {
        return productAvailabilities.stream()
                .map(this::mapProductAvailabilityToProductAvailabilityDto)
                .collect(Collectors.toList());
    }
}
