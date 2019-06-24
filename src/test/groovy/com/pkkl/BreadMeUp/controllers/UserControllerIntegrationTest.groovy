package com.pkkl.BreadMeUp.controllers

import com.pkkl.BreadMeUp.handlers.GlobalExceptionHandler
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import spock.lang.Specification

import javax.sql.DataSource

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@org.springframework.test.context.jdbc.Sql(scripts = "/clear.sql",
        executionPhase = org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerIntegrationTest extends Specification {

    private MockMvc mockMvc

    @Autowired
    private UserController userController

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler

    @Autowired
    private PasswordEncoder passwordEncoder

    @Autowired
    DataSource dataSource

    def setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
    }

    @WithMockUser(roles = "ADMIN", username = "admin")
    def "Should block user and return 200 response code when executor has admin role and does not try block yourself"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, false, 'email@email.email', 'login', 'password', '123456789', 1, null)")
        and:
        def userId = sql.firstRow("select user_id from users").getProperty('user_id')
        def roleId = sql.firstRow("select role_id from roles where roles.name='ROLE_ADMIN'").getProperty('role_id')
        and:
        sql.execute("insert into users_roles values(:userId, :roleId)", [userId: userId, roleId: roleId])
        when:
        def results = mockMvc.perform(
                put('/admin/users/login/block'))
                .andDo(print())

        then:
        results.andExpect(status().isOk())
        and:
        sql.rows('select blocked from users') == [[BLOCKED: true]]
    }

    @WithMockUser(roles = "ADMIN", username = "admin")
    def "Should not block user and should throw AccessDeniedException when executor has admin role and tries block yourself"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, false, 'email@email.email', 'admin', 'password', '123456789', 1, null)")
        and:
        def userId = sql.firstRow("select user_id from users").getProperty('user_id')
        def roleId = sql.firstRow("select role_id from roles where roles.name='ROLE_ADMIN'").getProperty('role_id')
        and:
        sql.execute("insert into users_roles values(:userId, :roleId)", [userId: userId, roleId: roleId])
        when:
        mockMvc.perform(
                put('/admin/users/admin/block'))
                .andDo(print())
        then:
        def e = thrown(NestedServletException.class)
        e.cause instanceof AccessDeniedException
        and:
        sql.rows('select blocked from users') == [[BLOCKED: false]]
    }

    @WithMockUser
    def "Should not block user and should throw AccessDeniedException when try to block user and executor without admin role"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, false, 'email@email.email', 'login', 'password', '123456789', 1, null)")
        and:
        def userId = sql.firstRow("select user_id from users").getProperty('user_id')
        def roleId = sql.firstRow("select role_id from roles where roles.name='ROLE_ADMIN'").getProperty('role_id')
        and:
        sql.execute("insert into users_roles values(:userId, :roleId)", [userId: userId, roleId: roleId])
        when:
        mockMvc.perform(
                put('/admin/users/login/block'))
                .andDo(print())
        then:
        def e = thrown(NestedServletException.class)
        e.cause instanceof AccessDeniedException
        and:
        sql.rows('select blocked from users') == [[BLOCKED: false]]
    }

    @WithMockUser(roles = "ADMIN", username = "admin")
    def "Should unblock user and return 200 response code when executor has admin role and does not try block yourself"() {
        given:
        Sql sql = new Sql(dataSource)
        sql.execute("insert into users values(1, true, 'email@email.email', 'login', 'password', '123456789', 1, null)")
        and:
        def userId = sql.firstRow("select user_id from users").getProperty('user_id')
        def roleId = sql.firstRow("select role_id from roles where roles.name='ROLE_ADMIN'").getProperty('role_id')
        and:
        sql.execute("insert into users_roles values(:userId, :roleId)", [userId: userId, roleId: roleId])
        when:
        def results = mockMvc.perform(
                put('/admin/users/login/unblock'))
                .andDo(print())

        then:
        results.andExpect(status().isOk())
        sql.rows('select blocked from users') == [[BLOCKED: false]]
    }

    @WithMockUser(roles = "ADMIN", username = "admin")
    def "Should not unblock user and should throw AccessDeniedException when executor has admin role and tries unblock yourself"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, true, 'email@email.email', 'admin', 'password', '123456789', 1, null)")
        and:
        def userId = sql.firstRow("select user_id from users").getProperty('user_id')
        def roleId = sql.firstRow("select role_id from roles where roles.name='ROLE_ADMIN'").getProperty('role_id')
        and:
        sql.execute("insert into users_roles values(:userId, :roleId)", [userId: userId, roleId: roleId])
        when:
        mockMvc.perform(
                put('/admin/users/admin/block'))
                .andDo(print())
        then:
        def e = thrown(NestedServletException.class)
        e.cause instanceof AccessDeniedException
        and:
        sql.rows('select blocked from users') == [[BLOCKED: true]]
    }

    @WithMockUser
    def "Should not unblock user and should throw AccessDeniedException when try to unblock user by executor without admin role"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, true, 'email@email.email', 'login', 'password', '123456789', 1, null)")
        and:
        def userId = sql.firstRow("select user_id from users").getProperty('user_id')
        def roleId = sql.firstRow("select role_id from roles where roles.name='ROLE_ADMIN'").getProperty('role_id')
        and:
        sql.execute("insert into users_roles values(:userId, :roleId)", [userId: userId, roleId: roleId])
        when:
        mockMvc.perform(
                put('/admin/users/login/block'))
                .andDo(print())
        then:
        def e = thrown(NestedServletException.class)
        e.cause instanceof AccessDeniedException
        and:
        sql.rows('select blocked from users') == [[BLOCKED: true]]
    }
}