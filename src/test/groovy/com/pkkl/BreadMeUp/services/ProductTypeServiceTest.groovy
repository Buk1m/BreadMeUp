package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.model.ProductType
import com.pkkl.BreadMeUp.repositories.ProductTypeRepository
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

class ProductTypeServiceTest extends Specification {

    private ProductTypeRepository productTypeRepository = Mock(ProductTypeRepository.class)

    private ProductTypeService productTypeService

    def setup() {
        this.productTypeService = new ProductTypeServiceImpl(productTypeRepository)
    }

    def "Should return object when product exists"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.findById(1) >> Optional.of(productType)
        when:
        ProductType returnedProductType = productTypeService.getById(1)
        then:
        returnedProductType == productType
    }

    def "Should throw DatabaseException when product does not exist"() {
        given:
        productTypeRepository.findById(1) >> Optional.ofNullable(null)
        when:
        productTypeService.getById(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should return object's collection when products exist"() {
        given:
        ProductType productType1 = Mock(ProductType.class)
        ProductType productType2 = Mock(ProductType.class)
        and:
        productTypeRepository.findAll() >> List.of(productType1, productType2)
        when:
        List<ProductType> returnedProductTypes = productTypeService.getAll()
        then:
        returnedProductTypes.size() == 2
        returnedProductTypes.contains(productType1)
        returnedProductTypes.contains(productType2)
    }

    def "Should throw DatabaseException when repository findAll throws Exception"() {
        given:
        productTypeRepository.findAll() >> { throw new RuntimeException() }
        when:
        productTypeService.getAll()
        then:
        thrown(DatabaseException.class)
    }

    def "Should not throw exception when product deleted"() {
        given:
        productTypeRepository.deleteById(1)
        when:
        productTypeService.delete(1)
        then:
        noExceptionThrown()
    }

    def "Should throw DatabaseException when repository deleteById throws Exception"() {
        given:
        productTypeRepository.deleteById(1) >> { throw new RuntimeException() }
        when:
        productTypeService.delete(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should return saved object when product has been successfully updated"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.save(productType) >> productType
        when:
        ProductType returnedProductType = this.productTypeService.update(productType)
        then:
        returnedProductType == productType
    }

    def "Should update throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.save(_ as ProductType) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.productTypeService.update(productType)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.save(_ as ProductType) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.productTypeService.update(productType)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw DatabaseException when repository save throws exception"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.save(_ as ProductType) >> { p -> throw new RuntimeException("message") }
        when:
        this.productTypeService.update(productType)
        then:
        thrown(DatabaseException.class)
    }

    def "Should add return saved object when product has been successfully added"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.save(productType) >> productType
        when:
        ProductType returnedProductType = this.productTypeService.add(productType)
        then:
        returnedProductType == productType
    }

    def "Should add throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.save(_ as ProductType) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.productTypeService.add(productType)
        then:
        thrown(ConstraintException.class)
    }

    def "Should add throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.save(_ as ProductType) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.productTypeService.add(productType)
        then:
        thrown(ConstraintException.class)
    }

    def "Should add throw DatabaseException when repository save throws exception"() {
        given:
        ProductType productType = Mock(ProductType.class)
        and:
        productTypeRepository.save(_ as ProductType) >> { p -> throw new RuntimeException("message") }
        when:
        this.productTypeService.add(productType)
        then:
        thrown(DatabaseException.class)
    }
}