package com.pkkl.BreadMeUp.controllers

import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import com.pkkl.BreadMeUp.model.User
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RegistrationControllerIntegrationTest extends Specification {

    private MockMvc mockMvc

    @Autowired
    private RegistrationController registrationController

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler


    def setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
    }

    def "Should register user and return created object in json with 201 response code"() {
        given:
        Map request = [
                login   : 'login',
                password: 'password',
                email   : 'email@email.email',
                phone   : '123456789'
        ]

        when:
        def results = mockMvc.perform(
                post('/registration')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(request)))
                .andDo(print())

        then:
        results.andExpect(status().isCreated())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.login').value("login"))
        results.andExpect(jsonPath('$.email').value('email@email.email'))
        results.andExpect(jsonPath('$.phone').value('123456789'))
    }

    def "Should return 409 response code when the same user registers twice"() {
        given:
        Map request = [
                login   : 'login',
                password: 'password',
                email   : 'email@email.email',
                phone   : '123456789'
        ]
        when:
        mockMvc.perform(
                post('/registration')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(request)))
                .andDo(print())

        def results = mockMvc.perform(
                post('/registration')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(request)))
                .andDo(print())
        then:
        results.andExpect(status().isConflict())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$').exists())
    }
}