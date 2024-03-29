package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.exceptions.NotFoundException
import com.pkkl.BreadMeUp.model.Bakery
import com.pkkl.BreadMeUp.model.Role
import com.pkkl.BreadMeUp.model.User
import com.pkkl.BreadMeUp.repositories.BakeryRepository
import com.pkkl.BreadMeUp.repositories.RoleRepository
import com.pkkl.BreadMeUp.repositories.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

class UserServiceTest extends Specification {

    private UserRepository userRepositoryMock = Mock(UserRepository.class)

    private RoleRepository roleRepositoryMock = Mock(RoleRepository.class)

    private PasswordEncoder passwordEncoder = Spy(BCryptPasswordEncoder.class)

    private UserService userService

    def setup() {
        this.userService = new UserServiceImpl(this.userRepositoryMock, this.roleRepositoryMock, this.passwordEncoder)
    }

    def "Should return saved object when user has been successfully registered"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        Role role = Mock(Role.class)
        and:
        roleRepositoryMock.findByName("ROLE_USER") >> Optional.of(role)
        userRepositoryMock.save(user) >> user
        when:
        User returnedUser = this.userService.register(user)
        then:
        returnedUser == user
    }

    def "Should throw DatabaseException when role does not exist"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        and:
        roleRepositoryMock.findByName("ROLE_USER") >> Optional.empty()
        when:
        this.userService.register(user)
        then:
        thrown(DatabaseException.class)
    }

    def "Should throw ConstraintException when repository throws InvalidDataAccessApiUsageException"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        Role role = Mock(Role.class)
        and:
        roleRepositoryMock.findByName("ROLE_USER") >> Optional.of(role)
        userRepositoryMock.save(_ as User) >> { u -> throw new InvalidDataAccessApiUsageException("Message") }
        when:
        this.userService.register(user)
        then:
        thrown(ConstraintException.class)
    }

    def "Should throw ConstraintException when repository throws ConstraintViolationException"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        Role role = Mock(Role.class)
        and:
        roleRepositoryMock.findByName("ROLE_USER") >> Optional.of(role)
        userRepositoryMock.save(_ as User) >>
                { u -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.userService.register(user)
        then:
        thrown(ConstraintException.class)
    }

    def "Should throw ConstraintException when repository throws exception caused by ConstraintViolationException"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        Role role = Mock(Role.class)
        and:
        roleRepositoryMock.findByName("ROLE_USER") >> Optional.of(role)
        userRepositoryMock.save(_ as User) >>
                { u -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.userService.register(user)
        then:
        thrown(ConstraintException.class)
    }

    def "Should return saved object when manager has been successfully registered"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        Role role = Mock(Role.class)
        and:
        roleRepositoryMock.findByName("ROLE_MANAGER") >> Optional.of(role)
        userRepositoryMock.save(user) >> user
        when:
        User returnedUser = this.userService.registerManager(user)
        then:
        returnedUser == user
    }

    def "Should throw DatabaseException when manager role does not exist"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        and:
        roleRepositoryMock.findByName("ROLE_MANAGER") >> Optional.empty()
        when:
        this.userService.registerManager(user)
        then:
        thrown(DatabaseException.class)
    }

    def "Should throw ConstraintException when repository throws InvalidDataAccessApiUsageException when registering manager"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        Role role = Mock(Role.class)
        and:
        roleRepositoryMock.findByName("ROLE_MANAGER") >> Optional.of(role)
        userRepositoryMock.save(_ as User) >> { u -> throw new InvalidDataAccessApiUsageException("Message") }
        when:
        this.userService.registerManager(user)
        then:
        thrown(ConstraintException.class)
    }

    def "Should throw ConstraintException when repository throws ConstraintViolationException when registering manager"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        Role role = Mock(Role.class)
        and:
        roleRepositoryMock.findByName("ROLE_MANAGER") >> Optional.of(role)
        userRepositoryMock.save(_ as User) >>
                { u -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.userService.registerManager(user)
        then:
        thrown(ConstraintException.class)
    }

    def "Should throw ConstraintException when repository throws exception caused by ConstraintViolationException when registering manager"() {
        given:
        User user = User.builder()
                .password("password")
                .build()
        Role role = Mock(Role.class)
        and:
        roleRepositoryMock.findByName("ROLE_MANAGER") >> Optional.of(role)
        userRepositoryMock.save(_ as User) >>
                { u -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.userService.registerManager(user)
        then:
        thrown(ConstraintException.class)
    }

    def "Should not throw exception when block method from repository does not throw exception"() {
        when:
        this.userService.blockUser("login")
        then:
        1 * this.userRepositoryMock.setBlocked("login", true)
        and:
        notThrown(Exception.class)
    }

    def "Should throw DatabaseException when block method from repository throws exception"() {
        given:
        this.userRepositoryMock.setBlocked("login", true) >> { _
            ->
            throw new RuntimeException()
        }
        when:
        this.userService.blockUser("login")
        then:
        thrown(DatabaseException.class)
    }

    def "Should not throw exception when unblock method from repository does not throw exception"() {
        when:
        this.userService.unblockUser("login")
        then:
        1 * this.userRepositoryMock.setBlocked("login", false)
        and:
        notThrown(Exception.class)
    }

    def "Should throw DatabaseException when unblock method from repository throws exception"() {
        given:
        this.userRepositoryMock.setBlocked("login", false) >> { _
            ->
            throw new RuntimeException()
        }
        when:
        this.userService.unblockUser("login")
        then:
        thrown(DatabaseException.class)
    }

    def "Should not throw exception when assign bakery to user and both exist"() {
        given:
        User user = Mock(User.class)
        and:
        this.userRepositoryMock.findById(0) >> Optional.of(user)
        when:
        this.userService.assignBakeryToUser(0, 0)
        then:
        1 * this.userRepositoryMock.assignBakeryToUser(0, 0)
        and:
        notThrown(Exception.class)
    }

    def "Should throw NotFoundException when assign bakery to user and user does not exist"() {
        given:
        this.userRepositoryMock.findById(0) >> Optional.ofNullable(null)
        when:
        this.userService.assignBakeryToUser(0, 0)
        then:
        thrown(NotFoundException.class)
    }

    def "Should throw NotFoundException when assign bakery to user and bakery does not exist"() {
        given:
        User user = Mock(User.class)
        and:
        this.userRepositoryMock.findById(0) >> Optional.of(user)
        and:
        this.userRepositoryMock.assignBakeryToUser(0,0) >> { _
            ->
            throw new DataIntegrityViolationException(null, null)
        }
        when:
        this.userService.assignBakeryToUser(0, 0)
        then:
        thrown(NotFoundException.class)
    }

    def "Should throw DatabaseException when assign bakery to user method from repository throws exception"() {
        given:
        User user = Mock(User.class)
        and:
        this.userRepositoryMock.findById(0) >> Optional.of(user)
        and:
        this.userRepositoryMock.assignBakeryToUser(0, 0) >> { _
            ->
            throw new RuntimeException()
        }
        when:
        this.userService.assignBakeryToUser(0, 0)
        then:
        thrown(DatabaseException.class)
    }
}