package com.pkkl.BreadMeUp.security

import groovy.json.JsonOutput
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import javax.sql.DataSource

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.test.context.jdbc.Sql(scripts = "/clear.sql",
        executionPhase = org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LoginIntegrationTest extends Specification {

    @Autowired
    private WebApplicationContext webApplicationContext

    @Autowired
    private SecurityConfig securityConfig

    @Autowired
    private JwtAuthSettings jwtAuthSettings

    @Autowired
    private PasswordEncoder passwordEncoder

    private MockMvc mockMvc

    @Autowired
    private DataSource dataSource

    def setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.webApplicationContext)
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
                        this.securityConfig.authenticationManagerBean(), this.jwtAuthSettings))
                .build()
    }

    def "Should return in authorization header token and 200 status code"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, false, 'email@email.email', 'login', :password, '123456789', 1)",
                [password:  passwordEncoder.encode("password")])
        and:
        def userId = sql.firstRow("select user_id from users").getProperty('user_id')
        def roleId = sql.firstRow("select role_id from roles").getProperty('role_id')
        and:
        sql.execute("insert into users_roles values(:userId, :roleId)", [userId: userId, roleId: roleId])
        and:
        Map request = [
                login   : 'login',
                password: 'password'
        ]
        when:
        def results = mockMvc.perform(
                post('/login')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(request)))
                .andDo(print())
        then:
        results.andExpect(status().isOk())
        results.andExpect(header().exists("Authorization"))
    }

    def "Should return 401 status code"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, false, 'email@email.email', 'login', :password, '123456789', 1)",
                [password:  passwordEncoder.encode("password")])
        and:
        Map request = [
                login   : 'incorrectLogin',
                password: 'password'
        ]
        when:
        def results = mockMvc.perform(
                post('/login')
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonOutput.toJson(request)))
                .andDo(print())
        then:
        results.andExpect(status().isUnauthorized())
        results.andExpect(header().doesNotExist("Authorization"))
    }
}
