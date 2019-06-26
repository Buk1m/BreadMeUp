package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.ProductAvailabilityDetailsDto;
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
    public ProductAvailabilityDetailsDto getById(@PathVariable(name = "id") int id) {
        return mapProductAvailabilityToProductAvailabilityDetailsDto(
                this.productAvailabilityService.getById(id)
        );
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductAvailabilityDetailsDto update(@PathVariable(name = "id") int id,
                                         @Valid @RequestBody final ProductAvailabilityDto productAvailabilityDto) {
        productAvailabilityDto.setId(id);
        return this.mapProductAvailabilityToProductAvailabilityDetailsDto(
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
    public ProductAvailabilityDetailsDto add(@Valid @RequestBody final ProductAvailabilityDto productAvailabilityDto) {
        return this.mapProductAvailabilityToProductAvailabilityDetailsDto(
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
    public ProductAvailabilityDetailsDto getByDateOrDateAndProduct(@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate localDate,
                                                            @RequestParam(name = "productId") Integer productId) {
        return this.mapProductAvailabilityToProductAvailabilityDetailsDto(
                this.productAvailabilityService.getByDateAndProduct(localDate, productId)
        );
    }

    private ProductAvailability mapProductAvailabilityDtoToProductAvailability(final ProductAvailabilityDto productAvailabilityDto) {
        return this.modelMapper.map(productAvailabilityDto, ProductAvailability.class);
    }

    private ProductAvailabilityDetailsDto mapProductAvailabilityToProductAvailabilityDetailsDto(final ProductAvailability productAvailability) {
        return this.modelMapper.map(productAvailability, ProductAvailabilityDetailsDto.class);
    }
}
