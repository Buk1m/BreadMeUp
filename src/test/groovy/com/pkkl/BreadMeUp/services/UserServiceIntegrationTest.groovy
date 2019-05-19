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
class UserServiceIntegrationTest extends Specification {

    @Autowired
    private UserService userService

    @Autowired
    DataSource dataSource

    def "Test user registration"() {
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
        sql.rows('select users.user_id, login, password, email, phone, name from users inner join users_roles on users.user_id = users_roles.user_id inner join roles on users_roles.role_id = roles.role_id') ==
                [[USER_ID: 1, LOGIN: "login", PASSWORD: "password", EMAIL: "email@email.email", PHONE: "123456789", NAME: "ROLE_USER"]]

        registeredUser.login == "login"
        registeredUser.password == "password"
        registeredUser.email == "email@email.email"
        registeredUser.phone == "123456789"
        registeredUser.getRoles().stream().map({ r -> r.getName() }).toArray() == ["ROLE_USER"]
    }

    def "Test invalid user data"() {
        given:
        User user = User.builder()
                .build()
        when:
        this.userService.register(user)
        then:
        final ConstraintException exception = thrown()
    }

    def "Test user registration twice"() {
        given:
        User user = User.builder()
                .login("login")
                .password("password")
                .email("email@email.email")
                .phone("123456789").build()
        when:
        this.userService.register(user)
        this.userService.register(user)
        then:
        final ConstraintException exception = thrown()
    }
}
