package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.model.Product
import com.pkkl.BreadMeUp.model.ProductAvailability
import com.pkkl.BreadMeUp.repositories.ProductAvailabilityRepository
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import java.time.LocalDate

class ProductAvailabilityServiceTest extends Specification{

    private ProductAvailabilityRepository productAvailabilityRepository = Mock(ProductAvailabilityRepository.class)

    private ProductAvailabilityService productAvailabilityService

    def setup(){
        productAvailabilityService = new ProductAvailabilityServiceImpl(productAvailabilityRepository)
    }

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

    def "Should return object's collection when product availability exist"() {
        given:
        ProductAvailability productAvailability1 = Mock(ProductAvailability.class)
        ProductAvailability productAvailability2 = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.findAll() >> List.of(productAvailability1, productAvailability2)
        when:
        List<ProductAvailability> returnedProductAvailabilities = productAvailabilityService.getAll()
        then:
        returnedProductAvailabilities.size() == 2
        returnedProductAvailabilities.contains(productAvailability1)
        returnedProductAvailabilities.contains(productAvailability2)
    }

    def "Should throw DatabaseException when repository findAll throws Exception"() {
        given:
        productAvailabilityRepository.findAll() >> {throw new RuntimeException()}
        when:
        productAvailabilityService.getAll()
        then:
        thrown(DatabaseException.class)
    }

    def "Should not throw exception when product availability deleted"() {
        given:
        productAvailabilityRepository.deleteById(1)
        when:
        productAvailabilityService.delete(1)
        then:
        noExceptionThrown()
    }

    def "Should throw DatabaseException when repository deleteById throws Exception"() {
        given:
        productAvailabilityRepository.deleteById(1) >> { throw new RuntimeException()}
        when:
        productAvailabilityService.delete(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should return saved object when product availability has been successfully updated"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.save(productAvailability) >> productAvailability
        when:
        ProductAvailability returnedProductAvailability = this.productAvailabilityService.update(productAvailability)
        then:
        returnedProductAvailability == productAvailability
    }

    def "Should update throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.productAvailabilityService.update(productAvailability)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.productAvailabilityService.update(productAvailability)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw DatabaseException when repository save throws exception"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new RuntimeException("message") }
        when:
        this.productAvailabilityService.update(productAvailability)
        then:
        thrown(DatabaseException.class)
    }

    def "Should add return saved object when product availability has been successfully added"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.save(productAvailability) >> productAvailability
        when:
        ProductAvailability returnedProductAvailability = this.productAvailabilityService.add(productAvailability)
        then:
        returnedProductAvailability == productAvailability
    }

    def "Should add throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.productAvailabilityService.add(productAvailability)
        then:
        thrown(ConstraintException.class)
    }

    def "Should add throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.productAvailabilityService.add(productAvailability)
        then:
        thrown(ConstraintException.class)
    }

    def "Should add throw DatabaseException when repository save throws exception"() {
        given:
        ProductAvailability productAvailability = Mock(ProductAvailability.class)
        and:
        productAvailabilityRepository.save(_ as ProductAvailability) >> { p -> throw new RuntimeException("message") }
        when:
        this.productAvailabilityService.add(productAvailability)
        then:
        thrown(DatabaseException.class)
    }

    def "Should getByProduct return product availabilities collection with given product"() {
        given:
        Product product1 = Product.builder().id(1).build()
        ProductAvailability productAvailability1 = ProductAvailability.builder().product(product1).build()
        Product product2= Product.builder().id(2).build()
        ProductAvailability productAvailability2 = ProductAvailability.builder().product(product2).build()
        and:
        productAvailabilityRepository.findAll() >> List.of(productAvailability1, productAvailability2)
        when:
        List<ProductAvailability> returnedProductAvailabilities = this.productAvailabilityService.getByProduct(1)
        then:
        returnedProductAvailabilities.size() == 1
        returnedProductAvailabilities.first() == productAvailability1
    }

    def "Should getByProduct throw DatabaseException when repository thrown exception"() {
        given:
        productAvailabilityRepository.findAll() >> {throw new RuntimeException()}
        when:
        this.productAvailabilityService.getByProduct(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should getByDate return product availabilities collection with given date"() {
        given:
        LocalDate localDate1 = LocalDate.now()
        ProductAvailability productAvailability1 = ProductAvailability.builder().date(localDate1).build()
        LocalDate localDate2 = LocalDate.now().plusDays(1)
        ProductAvailability productAvailability2 = ProductAvailability.builder().date(localDate2).build()
        and:
        productAvailabilityRepository.findAll() >> List.of(productAvailability1, productAvailability2)
        when:
        List<ProductAvailability> returnedProductAvailabilities = this.productAvailabilityService.getByDate(localDate1)
        then:
        returnedProductAvailabilities.size() == 1
        returnedProductAvailabilities.first() == productAvailability1
    }

    def "Should getByDate throw DatabaseException when repository thrown exception"() {
        given:
        LocalDate localDate = LocalDate.now()
        and:
        productAvailabilityRepository.findAll() >> {throw new RuntimeException()}
        when:
        this.productAvailabilityService.getByDate(localDate)
        then:
        thrown(DatabaseException.class)
    }

}
