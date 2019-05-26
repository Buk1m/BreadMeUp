package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.model.User
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.test.context.support.WithMockUser
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
        and:
        User user = User.builder()
                .login("login")
                .password("password")
                .email("email@email.email")
                .phone("123456789").build()
        when:
        User registeredUser = this.userService.register(user)
        then:
        sql.rows('select login, email, phone, name from users inner join users_roles on users.user_id = users_roles.user_id inner join roles on users_roles.role_id = roles.role_id') ==
                [[LOGIN: "login", EMAIL: "email@email.email", PHONE: "123456789", NAME: "ROLE_USER"]]
        and:
        registeredUser.login == "login"
        registeredUser.email == "email@email.email"
        registeredUser.phone == "123456789"
        registeredUser.getRoles().stream().map({ r -> r.getName() }).toArray() == ["ROLE_USER"]
    }

    def "Should throw ConstraintException when user data is empty/invalid"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        User user = User.builder()
                .password("password")
                .build()
        when:
        this.userService.register(user)
        then:
        thrown(ConstraintException.class)
        and:
        sql.rows('select count(*) from users').get(0).getProperty('COUNT(*)') == 0
    }

    def "Should throw ConstraintException when the same user registers twice"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
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
        thrown(ConstraintException.class)
        and:
        sql.rows('select login, email, phone, name from users inner join users_roles on users.user_id = users_roles.user_id inner join roles on users_roles.role_id = roles.role_id') ==
                [[LOGIN: "login", EMAIL: "email@email.email", PHONE: "123456789", NAME: "ROLE_USER"]]
    }

    @WithMockUser(roles = "ADMIN", username = "admin")
    def "Should block user when executor has admin role and does not try block yourself"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, false, 'email@email.email', 'user', 'password', '123456789', 1)")
        when:
        this.userService.blockUser("user")
        then:
        notThrown(Exception.class)
        and:
        sql.rows('select blocked from users') == [[BLOCKED: true]]
    }

    @WithMockUser(roles = "ADMIN", username = "admin")
    def "Should not block user and should throw AccessDeniedException when executor has admin role and tries block yourself"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, false, 'email@email.email', 'admin', 'password', '123456789', 1)")
        when:
        this.userService.blockUser("admin")
        then:
        thrown(AccessDeniedException.class)
        and:
        sql.rows('select blocked from users') == [[BLOCKED: false]]
    }

    @WithMockUser
    def "Should not block user and should throw AccessDeniedException when try to block user and executor without admin role"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, false, 'email@email.email', 'user', 'password', '123456789', 1)")
        when:
        this.userService.blockUser("user")
        then:
        thrown(AccessDeniedException.class)
        and:
        sql.rows('select blocked from users') == [[BLOCKED: false]]
    }

    @WithMockUser(roles = "ADMIN", username = "admin")
    def "Should unblock user when executor has admin role and does not try block yourself"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, true, 'email@email.email', 'user', 'password', '123456789', 1)")
        when:
        this.userService.unblockUser("user")
        then:
        notThrown(Exception.class)
        and:
        sql.rows('select blocked from users') == [[BLOCKED: false]]
    }

    @WithMockUser(roles = "ADMIN", username = "admin")
    def "Should not unblock user and should throw AccessDeniedException when executor has admin role and tries unblock yourself"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, true, 'email@email.email', 'admin', 'password', '123456789', 1)")
        when:
        this.userService.blockUser("admin")
        then:
        thrown(AccessDeniedException.class)
        and:
        sql.rows('select blocked from users') == [[BLOCKED: true]]
    }

    @WithMockUser
    def "Should not unblock user and should throw AccessDeniedException when try to unblock user by executor without admin role"() {
        given:
        Sql sql = new Sql(dataSource)
        and:
        sql.execute("insert into users values(1, true, 'email@email.email', 'user', 'password', '123456789', 1)")
        when:
        this.userService.unblockUser("user")
        then:
        thrown(AccessDeniedException.class)
        and:
        sql.rows('select blocked from users') == [[BLOCKED: true]]
    }
}
