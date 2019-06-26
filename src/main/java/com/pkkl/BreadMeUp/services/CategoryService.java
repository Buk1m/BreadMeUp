package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(int id);
}
