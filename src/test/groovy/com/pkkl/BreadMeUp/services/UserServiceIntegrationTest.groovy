package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.model.User
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import javax.sql.DataSource

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.test.context.jdbc.Sql(scripts = "/clear.sql",
        executionPhase = org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserServiceIntegrationTest extends Specification {

    @Autowired
    private UserService userService

    @Autowired
    DataSource dataSource

    def "Should register user and return saved object"() {
        given:
        Sql sql = new Sql(dataSource)
        User user = User.builder()
                .login("login")
                .password("password")
                .email("email@email.email")
                .phone("123456789").build()
        when:
        User registeredUser = this.userService.register(user)
        then:
        sql.rows('select login, password, email, phone, name from users inner join users_roles on users.user_id = users_roles.user_id inner join roles on users_roles.role_id = roles.role_id') ==
                [[LOGIN: "login", PASSWORD: "password", EMAIL: "email@email.email", PHONE: "123456789", NAME: "ROLE_USER"]]

        registeredUser.login == "login"
        registeredUser.password == "password"
        registeredUser.email == "email@email.email"
        registeredUser.phone == "123456789"
        registeredUser.getRoles().stream().map({ r -> r.getName() }).toArray() == ["ROLE_USER"]
    }

    def "Should throw ConstraintException when user data is empty/invalid"() {
        given:
        Sql sql = new Sql(dataSource)
        User user = User.builder()
                .build()
        when:
        this.userService.register(user)
        then:
        final ConstraintException exception = thrown()
        sql.rows('select count(*) from users').get(0).getProperty('COUNT(*)') == 0
    }

    def "Should throw ConstraintException when the same user registers twice"() {
        given:
        Sql sql = new Sql(dataSource)
        User user = User.builder()
                .login("login")
                .password("password")
                .email("email@email.email")
                .phone("123456789").build()
        when:
        this.userService.register(user)
        user.setId(null)
        this.userService.register(user)
        then:
        final ConstraintException exception = thrown()
        sql.rows('select login, password, email, phone, name from users inner join users_roles on users.user_id = users_roles.user_id inner join roles on users_roles.role_id = roles.role_id') ==
                [[LOGIN: "login", PASSWORD: "password", EMAIL: "email@email.email", PHONE: "123456789", NAME: "ROLE_USER"]]
    }
}
