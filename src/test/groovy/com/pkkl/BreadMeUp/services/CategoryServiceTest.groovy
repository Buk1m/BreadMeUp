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

    def "Should return category with given id when call getById"() {
        given:
        Category category = Category.builder()
                .id(1)
                .build()
        and:
        this.categoryRepository.findById(1) >> Optional.of(category)
        when:
        Category returnedCategory= this.categoryService.getById(1)
        then:
        returnedCategory.getId() == 1
        returnedCategory == category
    }

    def "Should getById throw DatabaseException when categoryRepository throws exception"() {
        given:
        this.categoryRepository.findById(1) >> { Optional.ofNullable(null) }
        when:
        this.categoryService.getById(1)
        then:
        thrown(DatabaseException.class)
    }
}
