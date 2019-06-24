package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.model.Category
import com.pkkl.BreadMeUp.repositories.CategoryRepository
import spock.lang.Specification

class CategoryServiceTest extends Specification {

    private CategoryRepository categoryRepository = Mock(CategoryRepository.class)

    private CategoryService categoryService

    def setup() {
        this.categoryService = new CategoryServiceImpl(categoryRepository)
    }

    def "Should return category list when call getAll"() {
        given:
        Category category1 = Mock(Category.class)
        Category category2 = Mock(Category.class)
        and:
        this.categoryRepository.findAll() >> List.of(category1, category2)
        when:
        List<Category> returnedCategories = this.categoryService.getAll()
        then:
        returnedCategories.size() == 2
        returnedCategories.contains(category1)
        returnedCategories.contains(category2)
    }

    def "Should throw DatabaseException when categoryRepository throws exception"() {
        given:
        this.categoryRepository.findAll() >> { _ -> throw new RuntimeException() }
        when:
        this.categoryService.getAll()
        then:
        thrown(DatabaseException.class)
    }
}
