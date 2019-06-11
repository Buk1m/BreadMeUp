package com.pkkl.BreadMeUp.controllers

import com.pkkl.BreadMeUp.dtos.BakeryDto
import com.pkkl.BreadMeUp.dtos.CategoryDto
import com.pkkl.BreadMeUp.dtos.ProductDto
import com.pkkl.BreadMeUp.dtos.ProductTypeDto
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import com.pkkl.BreadMeUp.model.Product
import com.pkkl.BreadMeUp.services.ProductService
import com.pkkl.BreadMeUp.services.ProductServiceImpl
import groovy.json.JsonOutput
import org.modelmapper.ModelMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ProductControllerTest extends Specification {

    private MockMvc mockMvc

    private ProductController productController

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()

    private ProductService productService = Mock(ProductServiceImpl.class)

    private ModelMapper modelMapper = new ModelMapper()

    def setup() {
        this.productController = new ProductController(productService, modelMapper)
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
    }

    def "Should return object by id with code 200 code when product exists"() {
        given:
        Product product = Product.builder()
                .id(1)
                .build()
        and:
        this.productService.getById(1) >> product
        when:
        def results = mockMvc.perform(
                get('/products/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').value("1"))
    }

    def "Should return 500 response code when product service getById throws DatabaseException"() {
        given:
        this.productService.getById(1) >> { id -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/products/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return objects list with code 200 code when get all"() {
        given:
        def products = [
                Product.builder()
                        .id(1)
                        .build(),
                Product.builder()
                        .id(2)
                        .build()]
        and:
        this.productService.getAll() >> products
        when:
        def results = mockMvc.perform(
                get('/products')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$[0].id').value("1"))
                .andExpect(jsonPath('$[1].id').value("2"))
    }

    def "Should return 500 response code when product service getAll throws DatabaseException"() {
        given:
        this.productService.getAll() >> { _ -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/products')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return objects list with code 200 code when get by bakery id"() {
        given:
        def products = [
                Product.builder()
                        .id(1)
                        .build(),
                Product.builder()
                        .id(2)
                        .build()]
        and:
        this.productService.getByBakery(1) >> products
        when:
        def results = mockMvc.perform(
                get('/products/bakeries/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$[0].id').value("1"))
                .andExpect(jsonPath('$[1].id').value("2"))
    }

    def "Should return 500 response code when product service getByBakery throws DatabaseException"() {
        given:
        this.productService.getByBakery(1) >> { id -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/products/bakeries/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return objects list with code 200 code when get by category id"() {
        given:
        def products = [
                Product.builder()
                        .id(1)
                        .build(),
                Product.builder()
                        .id(2)
                        .build()]
        and:
        this.productService.getByCategory(1) >> products
        when:
        def results = mockMvc.perform(
                get('/products/categories/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$[0].id').value("1"))
                .andExpect(jsonPath('$[1].id').value("2"))
    }

    def "Should return 500 response code when product service getByCategory throws DatabaseException"() {
        given:
        this.productService.getByCategory(1) >> { id -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/products/categories/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return objects list with code 200 code when get by category id and bakery id"() {
        given:
        def products = [
                Product.builder()
                        .id(1)
                        .build(),
                Product.builder()
                        .id(2)
                        .build()]
        and:
        this.productService.getByCategoryAndBakery(1, 1) >> products
        when:
        def results = mockMvc.perform(
                get('/products/bakeries/1/categories/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$[0].id').value("1"))
                .andExpect(jsonPath('$[1].id').value("2"))
    }

    def "Should return 500 response code when product service getByCategoryAndBakery throws DatabaseException"() {
        given:
        this.productService.getByCategoryAndBakery(1, 1) >> { categoryId, bakeryId -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/products/bakeries/1/categories/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return created object with status 201 when call add"() {
        given:
        ProductDto productDto = ProductDto.builder()
                .active(true)
                .bakery(BakeryDto.builder()
                        .id(1)
                        .build())
                .category(CategoryDto.builder()
                        .id(1)
                        .build())
                .productType(ProductTypeDto.builder()
                        .id(1)
                        .build())
                .name("name")
                .limit(1)
                .price(1)
                .build()

        this.productService.add(_ as Product) >> { Product p ->
            p.setId(1)
            return p
        }
        when:
        def results = mockMvc.perform(
                post('/products')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productDto)))
                .andDo(print())
        then:
        results.andExpect(status().isCreated())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value('1'))
    }

    def "Should return 400 when add body is invalid"() {
        given:
        ProductDto productDto = ProductDto.builder()
                .build()

        when:
        def results = mockMvc.perform(
                post('/products')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productDto)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 500 when product service add throws DatabaseException"() {
        given:
        ProductDto productDto = ProductDto.builder()
                .active(true)
                .bakery(BakeryDto.builder()
                        .id(1)
                        .build())
                .category(CategoryDto.builder()
                        .id(1)
                        .build())
                .productType(ProductTypeDto.builder()
                        .id(1)
                        .build())
                .name("name")
                .limit(1)
                .price(1)
                .build()

        this.productService.add(_ as Product) >> { throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                post('/products')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productDto)))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return updated object with status 200 when call update"() {
        given:
        ProductDto productDto = ProductDto.builder()
                .active(true)
                .bakery(BakeryDto.builder()
                        .id(1)
                        .build())
                .category(CategoryDto.builder()
                        .id(1)
                        .build())
                .productType(ProductTypeDto.builder()
                        .id(1)
                        .build())
                .name("name")
                .limit(1)
                .price(1)
                .build()

        this.productService.update(_ as Product) >> { Product p ->
            p.setId(1)
            return p
        }
        when:
        def results = mockMvc.perform(
                put('/products/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productDto)))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value('1'))
    }

    def "Should return 400 when update body is invalid"() {
        given:
        ProductDto productDto = ProductDto.builder()
                .build()

        when:
        def results = mockMvc.perform(
                put('/products/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productDto)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 500 when product service update throws DatabaseException"() {
        given:
        ProductDto productDto = ProductDto.builder()
                .active(true)
                .bakery(BakeryDto.builder()
                        .id(1)
                        .build())
                .category(CategoryDto.builder()
                        .id(1)
                        .build())
                .productType(ProductTypeDto.builder()
                        .id(1)
                        .build())
                .name("name")
                .limit(1)
                .price(1)
                .build()

        this.productService.update(_ as Product) >> { throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                put('/products/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productDto)))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 204 when object deleted"() {
        when:
        def results = mockMvc.perform(
                delete('/products/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isNoContent())
        and:
        1 * this.productService.delete(1)
    }

    def "Should return 500 when product service delete throws DatabaseException"() {
        given:
        this.productService.delete(1) >> { _ -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                delete('/products/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }
}