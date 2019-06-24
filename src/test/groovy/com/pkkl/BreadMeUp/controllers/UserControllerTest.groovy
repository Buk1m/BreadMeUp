package com.pkkl.BreadMeUp.controllers

import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import com.pkkl.BreadMeUp.services.UserService
import com.pkkl.BreadMeUp.services.UserServiceImpl
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest extends Specification {

    private MockMvc mockMvc

    private UserController userController

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()

    private UserService userService = Mock(UserServiceImpl.class)

    def setup() {
        this.userController = new UserController(userService)
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
    }

    def "Should block user and return 200 response code"() {
        when:
        def results = mockMvc.perform(
                put('/admin/users/user/block'))
                .andDo(print())
        then:
        1 * this.userService.blockUser("user")
        and:
        results.andExpect(status().isOk())
    }

    def "Should throw NestedServletException caused by AccessDeniedException when block throws AccessDeniedException"() {
        given:
        this.userService.blockUser("user") >> {
            s -> throw new AccessDeniedException("")
        }
        when:
        mockMvc.perform(
                put('/admin/users/user/block'))
                .andDo(print())
        then:
        def e = thrown(NestedServletException.class)
        e.cause instanceof AccessDeniedException
    }

    def "Should unblock user and return 200 response code"() {
        when:
        def results = mockMvc.perform(
                put('/admin/users/user/unblock'))
                .andDo(print())
        then:
        1 * this.userService.unblockUser("user")
        and:
        results.andExpect(status().isOk())
    }

    def "Should throw NestedServletException caused by AccessDeniedException when unblock throws AccessDeniedException"() {
        given:
        this.userService.unblockUser("user") >> {
            s -> throw new AccessDeniedException("")
        }
        when:
        mockMvc.perform(
                put('/admin/users/user/unblock'))
                .andDo(print())
        then:
        def e = thrown(NestedServletException.class)
        e.cause instanceof AccessDeniedException
    }

    def "Should assign bakery to user and return 200 response code"() {
        when:
        def results = mockMvc.perform(
                put('/admin/users/1/bakeries/1'))
                .andDo(print())
        then:
        this.userService.assignBakeryToUser(1, 1)
        and:
        results.andExpect(status().isOk())
    }

    def "Should return 500 response code when bakery service getById throws DatabaseException"() {
        given:
        this.userService.assignBakeryToUser(1, 1) >> { i -> throw new DatabaseException() }
        when:
        def results = mockMvc.perform(
                put('/admin/users/1/bakeries/1'))
                .andDo(print())
        then:
        results.andExpect(status().isInternalServerError())
    }

}
