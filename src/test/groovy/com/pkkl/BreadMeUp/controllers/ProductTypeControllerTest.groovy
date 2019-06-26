package com.pkkl.BreadMeUp.controllers

import com.pkkl.BreadMeUp.dtos.ProductTypeDto
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import com.pkkl.BreadMeUp.model.ProductType
import com.pkkl.BreadMeUp.model.UnitOfMeasurement
import com.pkkl.BreadMeUp.services.ProductTypeService
import com.pkkl.BreadMeUp.services.ProductTypeServiceImpl
import groovy.json.JsonOutput
import org.modelmapper.ModelMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ProductTypeControllerTest extends Specification {

    private MockMvc mockMvc

    private ProductTypeController productTypeController

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()

    private ProductTypeService productTypeService = Mock(ProductTypeServiceImpl.class)

    private ModelMapper modelMapper = new ModelMapper()

    def setup() {
        this.productTypeController = new ProductTypeController(productTypeService, modelMapper)
        mockMvc = MockMvcBuilders
                .standaloneSetup(productTypeController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
    }

    def "Should return object by id with code 200 code when product type exists"() {
        given:
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        and:
        this.productTypeService.getById(1) >> productType
        when:
        def results = mockMvc.perform(
                get('/admin/types/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').value("1"))
    }

    def "Should return 500 response code when productType service getById throws DatabaseException"() {
        given:
        this.productTypeService.getById(1) >> { id -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/admin/types/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return objects list with code 200 code when get all"() {
        given:
        def productTypes = [
                ProductType.builder()
                        .id(1)
                        .build(),
                ProductType.builder()
                        .id(2)
                        .build()]
        and:
        this.productTypeService.getAll() >> productTypes
        when:
        def results = mockMvc.perform(
                get('/admin/types')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$[0].id').value("1"))
                .andExpect(jsonPath('$[1].id').value("2"))
    }

    def "Should return 500 response code when productType service getAll throws DatabaseException"() {
        given:
        this.productTypeService.getAll() >> { _ -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/admin/types')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return created object with status 201 when call add"() {
        given:
        ProductTypeDto productTypeDto = ProductTypeDto.builder()
                .size(1.0)
                .unitOfMeasurement(UnitOfMeasurement.GRAM)
                .build()
        and:
        this.productTypeService.add(_ as ProductType) >> { ProductType p ->
            p.setId(1)
            return p
        }
        when:
        def results = mockMvc.perform(
                post('/admin/types')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productTypeDto)))
                .andDo(print())
        then:
        results.andExpect(status().isCreated())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value('1'))
    }

    def "Should return 400 when add body is invalid"() {
        given:
        ProductTypeDto productTypeDto = ProductTypeDto.builder()
                .build()
        when:
        def results = mockMvc.perform(
                post('/admin/types')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productTypeDto)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 500 when productType service add throws DatabaseException"() {
        given:
        ProductTypeDto productTypeDto = ProductTypeDto.builder()
                .size(1.0)
                .unitOfMeasurement(UnitOfMeasurement.GRAM)
                .build()

        this.productTypeService.add(_ as ProductType) >> { throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                post('/admin/types')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productTypeDto)))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return updated object with status 200 when call update"() {
        given:
        ProductTypeDto productTypeDto = ProductTypeDto.builder()
                .size(1.0)
                .unitOfMeasurement(UnitOfMeasurement.GRAM)
                .build()
        and:
        this.productTypeService.update(_ as ProductType) >> { ProductType p ->
            p.setId(1)
            return p
        }
        when:
        def results = mockMvc.perform(
                put('/admin/types/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productTypeDto)))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value('1'))
    }

    def "Should return 400 when update body is invalid"() {
        given:
        ProductTypeDto productTypeDto = ProductTypeDto.builder()
                .build()
        when:
        def results = mockMvc.perform(
                put('/admin/types/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productTypeDto)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 500 when product service update throws DatabaseException"() {
        given:
        ProductTypeDto productTypeDto = ProductTypeDto.builder()
                .size(1.0)
                .unitOfMeasurement(UnitOfMeasurement.GRAM)
                .build()
        and:
        this.productTypeService.update(_ as ProductType) >> { throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                put('/admin/types/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(productTypeDto)))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 204 when object deleted"() {
        when:
        def results = mockMvc.perform(
                delete('/admin/types/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isNoContent())
        and:
        1 * this.productTypeService.delete(1)
    }

    def "Should return 500 when productType service delete throws DatabaseException"() {
        given:
        this.productTypeService.delete(1) >> { _ -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                delete('/admin/types/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }
}