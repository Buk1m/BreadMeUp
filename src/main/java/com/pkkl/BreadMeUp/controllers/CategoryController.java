package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.CategoryDto;
import com.pkkl.BreadMeUp.model.Category;
import com.pkkl.BreadMeUp.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class CategoryController {

    private final CategoryService categoryService;

    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(
            value = "/categories",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<CategoryDto> getAll() {
        return this.mapCategoryListToCategoryDtoList(
                this.categoryService.getAll()
        );
    }

    private List<CategoryDto> mapCategoryListToCategoryDtoList(final List<Category> categories) {
        return categories.stream()
                .map(this::mapCategoryToCategoryDto)
                .collect(Collectors.toList());
    }

    private CategoryDto mapCategoryToCategoryDto(Category category) {
        return this.modelMapper.map(category, CategoryDto.class);
    }
}
