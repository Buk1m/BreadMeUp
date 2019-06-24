package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.Category;
import com.pkkl.BreadMeUp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new DatabaseException(e);
        }
    }

    @Override
    public Category getById(int id) {
        try {
            return this.categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category type doesn't exist"));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }
}
