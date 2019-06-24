package com.pkkl.BreadMeUp.controllers

import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import com.pkkl.BreadMeUp.services.CategoryService
import org.modelmapper.ModelMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import com.pkkl.BreadMeUp.model.Category

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class CategoryControllerTest extends Specification {

    private MockMvc mockMvc

    private CategoryController categoryController

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()

    private CategoryService categoryService = Mock(CategoryService.class)

    private ModelMapper modelMapper = new ModelMapper()

    def setup() {
        this.categoryController = new CategoryController(categoryService, modelMapper)
        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
    }

    def "Should return objects list with code 200 code when get all"() {
        given:
        def categories = [
                Category.builder()
                        .id(1)
                        .build(),
                Category.builder()
                        .id(2)
                        .build()]
        and:
        this.categoryService.getAll() >> categories
        when:
        def results = mockMvc.perform(
                get('/categories')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$[0].id').value("1"))
                .andExpect(jsonPath('$[1].id').value("2"))
    }

    def "Should return 500 response code when category service getAll throws DatabaseException"() {
        given:
        this.categoryService.getAll() >> { _ -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/categories')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }
}
