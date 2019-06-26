package com.pkkl.BreadMeUp.security

import com.pkkl.BreadMeUp.model.Bakery
import com.pkkl.BreadMeUp.model.User
import org.springframework.security.core.userdetails.UserDetails
import spock.lang.Specification

class MethodSecurityExpressionTest extends Specification {

    def "Should return true if principal is bakery owner"() {
        given:
        UserDetails userDetails = new AuthUserDetails(
                User.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build())
                        .build()
        )
        and:
        MethodSecurityExpression methodSecurityExpression = new MethodSecurityExpression()
        when:
        boolean result = methodSecurityExpression.isThisBakeryManager(userDetails, 1)
        then:
        result
    }

    def "Should return false if principal is not bakery owner"() {
        given:
        UserDetails userDetails = new AuthUserDetails(
                User.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build())
                        .build()
        )
        and:
        MethodSecurityExpression methodSecurityExpression = new MethodSecurityExpression()
        when:
        boolean result = methodSecurityExpression.isThisBakeryManager(userDetails, 0)
        then:
        !result
    }

    def "Should return false if principal has not got bakery"() {
        given:
        UserDetails userDetails = new AuthUserDetails(
                User.builder()
                        .build())
        and:
        MethodSecurityExpression methodSecurityExpression = new MethodSecurityExpression()
        when:
        boolean result = methodSecurityExpression.isThisBakeryManager(userDetails, 1)
        then:
        !result
    }
}
