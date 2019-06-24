package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.model.Bakery
import com.pkkl.BreadMeUp.model.Product
import com.pkkl.BreadMeUp.model.ProductAvailability
import com.pkkl.BreadMeUp.repositories.ProductAvailabilityRepository
import com.pkkl.BreadMeUp.repositories.ProductRepository
import com.pkkl.BreadMeUp.security.MethodSecurityExpression
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
class ProductAvailabilityServiceTest extends Specification {

    @SpringBean
    private ProductAvailabilityRepository productAvailabilityRepository = Mock(ProductAvailabilityRepository.class)

    @SpringBean
    private ProductRepository productRepository = Mock(ProductRepository.class)

    @Autowired
    private ProductAvailabilityService productAvailabilityService

    @SpringBean
    MethodSecurityExpression methodSecurityExpression = Mock(MethodSecurityExpression.class)

    def "Should return object when product availability exists"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.findById(1) >> Optional.of(productAvailability)
        when:
        ProductAvailability returnedProductAvailability = productAvailabilityService.getById(1)
        then:
        productAvailability == returnedProductAvailability
    }

    def "Should throw DatabaseException when product availability does not exist"() {
        given:
        productAvailabilityRepository.findById(1) >> Optional.ofNullable(null)
        when:
        productAvailabilityService.getById(1)
        then:
        thrown(DatabaseException.class)
    }

    @WithMockUser(roles = "MANAGER")
    def "Should return saved object when product availability has been successfully updated when user is manager and is bakery owner"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> true
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        and:
        productAvailabilityRepository.save(productAvailability) >> productAvailability
        when:
        ProductAvailability returnedProductAvailability = this.productAvailabilityService.update(productAvailability)
        then:
        returnedProductAvailability == productAvailability
    }

    @WithMockUser(roles = "MANAGER")
    def "Should throw AccessDeniedException and user is manager and is not bakery owner"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> false
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        when:
        this.productAvailabilityService.update(productAvailability)
        then:
        thrown(AccessDeniedException.class)
    }

    @WithMockUser(roles = "MANAGER")
    def "Should update throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> true
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.productAvailabilityService.update(productAvailability)
        then:
        thrown(ConstraintException.class)
    }

    @WithMockUser(roles = "MANAGER")
    def "Should update throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> true
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.productAvailabilityService.update(productAvailability)
        then:
        thrown(ConstraintException.class)
    }

    @WithMockUser(roles = "MANAGER")
    def "Should update throw DatabaseException when repository save throws exception"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> true
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new RuntimeException("message") }
        when:
        this.productAvailabilityService.update(productAvailability)
        then:
        thrown(DatabaseException.class)
    }

    @WithMockUser(roles = "MANAGER")
    def "Should add return saved object when product availability has been successfully added and user is manager and is bakery owner"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> true
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        and:
        productAvailabilityRepository.save(productAvailability) >> productAvailability
        when:
        ProductAvailability returnedProductAvailability = this.productAvailabilityService.add(productAvailability)
        then:
        returnedProductAvailability == productAvailability
    }

    @WithMockUser(roles = "MANAGER")
    def "Should add return saved object when product availability has been successfully added and user is manager and is not bakery owner"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> false
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        when:
        this.productAvailabilityService.add(productAvailability)
        then:
        thrown(AccessDeniedException.class)
    }

    @WithMockUser(roles = "MANAGER")
    def "Should add throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> true
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.productAvailabilityService.add(productAvailability)
        then:
        thrown(ConstraintException.class)
    }

    @WithMockUser(roles = "MANAGER")
    def "Should add throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> true
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.productAvailabilityService.add(productAvailability)
        then:
        thrown(ConstraintException.class)
    }

    @WithMockUser(roles = "MANAGER")
    def "Should add throw DatabaseException when repository save throws exception"() {
        given:
        methodSecurityExpression.isThisBakeryManager(_ as UserDetails, 1) >> true
        and:
        ProductAvailability productAvailability = ProductAvailability.builder()
                .product(Product.builder()
                        .bakery(Bakery.builder()
                                .id(1)
                                .build()
                        ).build()
                ).build()
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new RuntimeException("message") }
        when:
        this.productAvailabilityService.add(productAvailability)
        then:
        thrown(DatabaseException.class)
    }

    def "Should getByDateAndProduct return product availability"() {
        given:
        Product product1 = Product.builder().id(1).build()
        LocalDate localDate1 = LocalDate.now()
        Product product2 = Product.builder().id(2).build()
        LocalDate localDate2 = LocalDate.now().plusDays(1)
        and:
        ProductAvailability productAvailability1 = ProductAvailability.builder().product(product1).date(localDate1).build()
        ProductAvailability productAvailability2 = ProductAvailability.builder().product(product2).date(localDate2).build()
        ProductAvailability productAvailability3 = ProductAvailability.builder().product(product2).date(localDate1).build()
        ProductAvailability productAvailability4 = ProductAvailability.builder().product(product1).date(localDate2).build()
        and:
        productAvailabilityRepository.findAll() >> List.of(productAvailability1, productAvailability2,
                productAvailability3, productAvailability4)
        when:
        ProductAvailability returnedProductAvailability = this.productAvailabilityService
                .getByDateAndProduct(localDate1, product1.getId())
        then:
        returnedProductAvailability == productAvailability1
    }

    def "Should getByDateAndProduct return product availabilities created from product"() {
        given:
        Product product1 = Product.builder().id(1).build()
        LocalDate localDate1 = LocalDate.now()
        Product product2 = Product.builder().id(2).build()
        LocalDate localDate2 = LocalDate.now().plusDays(1)
        and:
        ProductAvailability productAvailability1 = ProductAvailability.builder().product(product2).date(localDate2).build()
        ProductAvailability productAvailability2 = ProductAvailability.builder().product(product2).date(localDate1).build()
        ProductAvailability productAvailability3 = ProductAvailability.builder().product(product1).date(localDate2).build()
        and:
        productAvailabilityRepository.findAll() >> List.of(productAvailability1, productAvailability2,
                productAvailability3)
        and:
        Product productFromDatabase = Product.builder()
                .id(1)
                .limit(10)
                .build()
        and:
        this.productRepository.findById(1) >> Optional.of(productFromDatabase)
        when:
        ProductAvailability returnedProductAvailability = this.productAvailabilityService
                .getByDateAndProduct(localDate1, product1.getId())
        then:
        returnedProductAvailability.getDate() == localDate1
        returnedProductAvailability.getOrderedNumber() == 0
        returnedProductAvailability.getLimit() == 10
        returnedProductAvailability.getProduct() == productFromDatabase
    }

    def "Should getByDateAndProduct throw DatabaseException when repository thrown exception"() {
        given:
        Product product = Product.builder().id(1).build()
        LocalDate localDate = LocalDate.now()
        and:
        productAvailabilityRepository.findAll() >> { throw new RuntimeException() }
        when:
        this.productAvailabilityService.getByDateAndProduct(localDate, product.getId())
        then:
        thrown(DatabaseException.class)
    }

    def "Should getAllByProduct return list of product availabilities"() {
        given:
        Product product1 = Product.builder().id(1).build()
        Product product2 = Product.builder().id(2).build()
        and:
        ProductAvailability productAvailability1 = ProductAvailability.builder().product(product1).build()
        ProductAvailability productAvailability2 = ProductAvailability.builder().product(product2).build()
        ProductAvailability productAvailability3 = ProductAvailability.builder().product(product1).build()
        and:
        productAvailabilityRepository.findAll() >> List.of(productAvailability1, productAvailability2, productAvailability3)
        when:
        List<ProductAvailability> returnedProductAvailabilities = this.productAvailabilityService
                .getAllByProduct(1)
        then:
        returnedProductAvailabilities.size() == 2
        returnedProductAvailabilities.contains(productAvailability1)
        returnedProductAvailabilities.contains(productAvailability3)
    }


    def "Should getAllByProduct throw DatabaseException when repository thrown exception"() {
        given:
        productAvailabilityRepository.findAll() >> { throw new RuntimeException() }
        when:
        this.productAvailabilityService.getAllByProduct(1)
        then:
        thrown(DatabaseException.class)
    }
}
