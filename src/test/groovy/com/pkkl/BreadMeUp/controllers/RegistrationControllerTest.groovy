package com.pkkl.BreadMeUp.controllers

import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import com.pkkl.BreadMeUp.model.User
import com.pkkl.BreadMeUp.services.UserService
import com.pkkl.BreadMeUp.services.UserServiceImpl
import groovy.json.JsonOutput
import org.modelmapper.ModelMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class RegistrationControllerTest extends Specification {

    private MockMvc mockMvc

    private RegistrationController registrationController

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()

    private UserService userService = Mock(UserServiceImpl.class)

    private ModelMapper modelMapper = new ModelMapper()

    def setup() {
        this.registrationController = new RegistrationController(userService, modelMapper)
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
        and:
        this.userService.register(_ as User) >> User.builder()
                .id(1)
                .login("login")
                .password("password")
                .email("email@email.email")
                .phone("123456789")
                .build()
        when:
        def results = mockMvc.perform(
                post('/registration')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(request)))
                .andDo(print())

        then:
        results.andExpect(status().isCreated())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$.id').value("1"))
        results.andExpect(jsonPath('$.login').value("login"))
        results.andExpect(jsonPath('$.password').doesNotExist())
        results.andExpect(jsonPath('$.email').value('email@email.email'))
        results.andExpect(jsonPath('$.phone').value('123456789'))
    }

    def "Should return 400 response code when user data is empty/invalid"() {
        given:
        User emptyUser = new User()
        when:
        def results = mockMvc.perform(
                post('/registration')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(emptyUser)))
                .andDo(print())
        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    def "Should return 409 response code when user service throws ConflictException"() {
        given:
        Map request = [
                login   : 'login',
                password: 'password',
                email   : 'email@email.email',
                phone   : '123456789'
        ]
        and:
        this.userService.register(_ as User) >> { u -> throw new ConstraintException() }
        when:
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

    def "Should return 500 response code when user service throws DatabaseException"() {
        given:
        Map request = [
                login   : 'login',
                password: 'password',
                email   : 'email@email.email',
                phone   : '123456789'
        ]
        and:
        this.userService.register(_ as User) >> { u -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                post('/registration')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(request)))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
        results.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        results.andExpect(jsonPath('$').exists())
    }
}