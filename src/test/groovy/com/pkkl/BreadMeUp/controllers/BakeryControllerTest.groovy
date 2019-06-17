package com.pkkl.BreadMeUp.controllers

import com.pkkl.BreadMeUp.dtos.BakeryDto
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import com.pkkl.BreadMeUp.model.Bakery
import com.pkkl.BreadMeUp.services.BakeryService
import com.pkkl.BreadMeUp.services.BakeryServiceImpl
import groovy.json.JsonOutput
import org.modelmapper.ModelMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class BakeryControllerTest extends Specification {
    private MockMvc mockMvc

    private BakeryController bakeryController

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()

    private BakeryService bakeryService = Mock(BakeryServiceImpl.class)

    private ModelMapper modelMapper = new ModelMapper()

    def setup() {
        this.bakeryController = new BakeryController(bakeryService, modelMapper)
        mockMvc = MockMvcBuilders
                .standaloneSetup(bakeryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
    }

    def "Should return object by id with code 200 code when bakery exists"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        and:
        this.bakeryService.getById(1) >> bakery
        when:
        def results = mockMvc.perform(
                get('/bakeries/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').value("1"))
    }

    def "Should return 500 response code when bakery service getById throws DatabaseException"() {
        given:
        this.bakeryService.getById(1) >> { id -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/bakeries/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return objects list with code 200 code when get all"() {
        given:
        def bakeries = [
                Bakery.builder()
                        .id(1)
                        .build(),
                Bakery.builder()
                        .id(2)
                        .build()]
        and:
        this.bakeryService.getAll() >> bakeries
        when:
        def results = mockMvc.perform(
                get('/bakeries')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$[0].id').value("1"))
                .andExpect(jsonPath('$[1].id').value("2"))
    }

    def "Should return 500 response code when bakery service getAll throws DatabaseException"() {
        given:
        this.bakeryService.getAll() >> { _ -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                get('/bakeries')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$').exists())
    }

    def "Should return created object with status 201 when call add"() {
        given:
        BakeryDto bakeryDto = BakeryDto.builder()
                .name("name")
                .city("city")
                .postalCode("91-366")
                .streetName("streetName")
                .streetNumber("streetNumber")
                .placeId("placeId")
                .id(1)
                .build()

        this.bakeryService.add(_ as Bakery) >> { Bakery p ->
            p.setId(1)
            return p
        }
        when:
        def results = mockMvc.perform(
                post('/bakeries')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(bakeryDto)))
                .andDo(print())
        then:
        results.andExpect(status().isCreated())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value('1'))
    }

    def "Should return 400 when add body is invalid"() {
        given:
        BakeryDto bakeryDto = BakeryDto.builder()
                .build()

        when:
        def results = mockMvc.perform(
                post('/bakeries')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(bakeryDto)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 500 when bakery service add throws DatabaseException"() {
        given:
        BakeryDto bakeryDto = BakeryDto.builder()
                .name("name")
                .city("city")
                .postalCode("91-366")
                .streetName("streetName")
                .streetNumber("streetNumber")
                .placeId("placeId")
                .id(1)
                .build()

        this.bakeryService.add(_ as Bakery) >> { throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                post('/bakeries')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(bakeryDto)))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return updated object with status 200 when call update"() {
        given:
        BakeryDto bakeryDto = BakeryDto.builder()
                .name("name")
                .city("city")
                .postalCode("91-366")
                .streetName("streetName")
                .streetNumber("streetNumber")
                .placeId("placeId")
                .id(1)
                .build()

        this.bakeryService.update(_ as Bakery) >> { Bakery p ->
            p.setId(1)
            return p
        }
        when:
        def results = mockMvc.perform(
                put('/bakeries/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(bakeryDto)))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value('1'))
    }

    def "Should return 400 when update body is invalid"() {
        given:
        BakeryDto bakeryDto = BakeryDto.builder()
                .build()

        when:
        def results = mockMvc.perform(
                put('/bakeries/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(bakeryDto)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 204 when object deleted"() {
        when:
        def results = mockMvc.perform(
                delete('/bakeries/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isNoContent())
        and:
        1 * this.bakeryService.delete(1)
    }

    def "Should return 500 when bakery service delete throws DatabaseException"() {
        given:
        this.bakeryService.delete(1) >> { _ -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                delete('/bakeries/1')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }
}
