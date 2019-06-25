package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.Category;
import com.pkkl.BreadMeUp.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        try {
            return this.categoryRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DatabaseException(e);
        }
    }

    @Override
    public Category getById(int id) {
        try {
            return this.categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category type doesn't exist"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DatabaseException(e);
        }
    }
}
