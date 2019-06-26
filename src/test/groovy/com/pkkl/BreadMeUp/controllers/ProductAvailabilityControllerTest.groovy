package com.pkkl.BreadMeUp.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.pkkl.BreadMeUp.dtos.ProductAvailabilityDto
import com.pkkl.BreadMeUp.dtos.ProductDto
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import com.pkkl.BreadMeUp.model.ProductAvailability
import com.pkkl.BreadMeUp.services.ProductAvailabilityService
import com.pkkl.BreadMeUp.services.ProductAvailabilityServiceImpl
import org.modelmapper.ModelMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ProductAvailabilityControllerTest extends Specification {

    private MockMvc mockMvc

    private ProductAvailabilityController productAvailabilityController

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()

    private ProductAvailabilityService productAvailabilityService = Mock(ProductAvailabilityServiceImpl.class)

    private ModelMapper modelMapper = new ModelMapper()

    def setup() {
        this.productAvailabilityController = new ProductAvailabilityController(this.productAvailabilityService, modelMapper)
        mockMvc = MockMvcBuilders
                .standaloneSetup(this.productAvailabilityController)
                .setControllerAdvice(this.globalExceptionHandler)
                .build()
    }

    def "Should return object by id with code 200 code when product availability exists"() {
        given:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .id(1)
                .build()
        and:
        this.productAvailabilityService.getById(1) >> productAvailability
        when:
        def results = mockMvc.perform(
                get('/availabilities/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').value("1"))
    }

    def "Should return 500 response code when productAvailability service getById throws DatabaseException"() {
        given:
        this.productAvailabilityService.getById(1) >> { id -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/availabilities/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return created object with status 201 when call add"() {
        given:
        ProductAvailabilityDto productAvailabilityDto = ProductAvailabilityDto.builder()
                .product(ProductDto.builder()
                        .id(1)
                        .build())
                .limit(1)
                .date(LocalDate.of(2019, 6, 13))
                .orderedNumber(1)
                .build()
        and:
        this.productAvailabilityService.add(_ as ProductAvailability) >> { ProductAvailability p ->
            p.setId(1)
            return p
        }
        and:
        ObjectMapper mapper = new ObjectMapper()
        when:
        def results = mockMvc.perform(
                post('/availabilities')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(productAvailabilityDto)))
                .andDo(print())
        then:
        results.andExpect(status().isCreated())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value('1'))
    }

    def "Should return 400 when add body is invalid"() {
        given:
        ProductAvailabilityDto productAvailabilityDto = ProductAvailabilityDto.builder()
                .build()
        and:
        ObjectMapper mapper = new ObjectMapper()
        when:
        def results = mockMvc.perform(
                post('/availabilities')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(productAvailabilityDto)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 500 when productAvailability service add throws DatabaseException"() {
        given:
        ProductAvailabilityDto productAvailabilityDto = ProductAvailabilityDto.builder()
                .product(ProductDto.builder()
                        .id(1)
                        .build())
                .limit(1)
                .date(LocalDate.now())
                .orderedNumber(1)
                .build()
        and:
        this.productAvailabilityService.add(_ as ProductAvailability) >> { throw new DatabaseException() }
        and:
        ObjectMapper mapper = new ObjectMapper()
        when:
        def results = mockMvc.perform(
                post('/availabilities')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(productAvailabilityDto)))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return updated object with status 200 when call update"() {
        given:
        ProductAvailabilityDto productAvailabilityDto = ProductAvailabilityDto.builder()
                .product(ProductDto.builder()
                        .id(1)
                        .build())
                .limit(1)
                .date(LocalDate.now())
                .orderedNumber(1)
                .build()
        and:
        this.productAvailabilityService.update(_ as ProductAvailability) >> { ProductAvailability p ->
            p.setId(1)
            return p
        }
        and:
        ObjectMapper mapper = new ObjectMapper()
        when:
        def results = mockMvc.perform(
                put('/availabilities/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(productAvailabilityDto)))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value('1'))
    }

    def "Should return 400 when update body is invalid"() {
        given:
        ProductAvailabilityDto productAvailabilityDto = ProductAvailabilityDto.builder()
                .build()
        and:
        ObjectMapper mapper = new ObjectMapper()
        when:
        def results = mockMvc.perform(
                put('/availabilities/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(productAvailabilityDto)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 500 when productAvailability service update throws DatabaseException"() {
        given:
        ProductAvailabilityDto productAvailabilityDto = ProductAvailabilityDto.builder()
                .product(ProductDto.builder()
                        .id(1)
                        .build())
                .limit(1)
                .date(LocalDate.now())
                .orderedNumber(1)
                .build()
        and:
        this.productAvailabilityService.update(_ as ProductAvailability) >> { throw new DatabaseException() }
        and:
        ObjectMapper mapper = new ObjectMapper()
        when:
        def results = mockMvc.perform(
                put('/availabilities/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(productAvailabilityDto)))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return list by date and product with code 200 code when product availabilities exist"() {
        given:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .id(1)
                .build()
        and:
        this.productAvailabilityService.getByDateAndProduct(LocalDate.of(2019, 6, 13), 1) >>
                productAvailability
        when:
        def results = mockMvc.perform(
                get('/availabilities/')
                        .param("date", "13-06-2019")
                        .param("productId", "1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').value("1"))
    }

    def "Should return 500 response code when productAvailability service getByDateAndProduct throws DatabaseException"() {
        this.productAvailabilityService.getByDateAndProduct(LocalDate.of(2019, 6, 13), 1) >>
                { throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/availabilities/')
                        .param("date", "13-06-2019")
                        .param("productId", "1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }
}