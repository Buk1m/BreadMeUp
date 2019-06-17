package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.BakeryDto;
import com.pkkl.BreadMeUp.model.Bakery;
import com.pkkl.BreadMeUp.services.BakeryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bakeries")
public class BakeryController {
    private final BakeryService bakeryService;

    private final ModelMapper modelMapper;

    @Autowired
    public BakeryController(BakeryService bakeryService, ModelMapper modelMapper) {
        this.bakeryService = bakeryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public BakeryDto getById(@PathVariable(name = "id") int id) {
        return mapBakeryToBakeryDto(
                bakeryService.getById(id)
        );
    }

    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<BakeryDto> getAll() {
        return mapBakeryListToBakeryDtoList(
                bakeryService.getAll()
        );
    }

    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id) {
        bakeryService.delete(id);
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public BakeryDto update(@PathVariable(name = "id") int id, @Valid @RequestBody final BakeryDto bakeryDto) {
        bakeryDto.setId(id);
        return mapBakeryToBakeryDto(
                bakeryService.update(
                        mapBakeryDtoToBakery(bakeryDto)
                )
        );
    }

    @PostMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BakeryDto add(@Valid @RequestBody final BakeryDto bakeryDto) {
        return mapBakeryToBakeryDto(
                bakeryService.add(
                        mapBakeryDtoToBakery(bakeryDto)
                )
        );
    }

    private Bakery mapBakeryDtoToBakery(final BakeryDto bakeryDto) {
        return modelMapper.map(bakeryDto, Bakery.class);
    }

    private BakeryDto mapBakeryToBakeryDto(final Bakery bakery) {
        return modelMapper.map(bakery, BakeryDto.class);
    }

    private List<BakeryDto> mapBakeryListToBakeryDtoList(final List<Bakery> bakeries) {
        return bakeries.stream()
                .map(this::mapBakeryToBakeryDto)
                .collect(Collectors.toList());
    }
}
