package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.model.*
import com.pkkl.BreadMeUp.repositories.ProductAvailabilityRepository
import com.pkkl.BreadMeUp.repositories.ProductRepository
import com.pkkl.BreadMeUp.security.AuthUserDetails
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

class ProductServiceTest extends Specification {

    private ProductRepository productRepository = Mock(ProductRepository.class)

    private BakeryService bakeryService = Mock(BakeryService.class)

    private CategoryService categoryService = Mock(CategoryService.class)

    private ProductTypeService productTypeService = Mock(ProductTypeService.class)

    private ProductAvailabilityRepository productAvailabilityRepository = Mock(ProductAvailabilityRepository.class)

    private ProductService productService

    def setup() {
        this.productService = new ProductServiceImpl(productRepository, bakeryService, categoryService, productTypeService, productAvailabilityRepository)
    }

    def "Should return object when product exists"() {
        given:
        Product product = Mock(Product.class)
        and:
        productRepository.findById(1) >> Optional.of(product)
        when:
        Product returnedProduct = productService.getById(1)
        then:
        returnedProduct == product
    }

    def "Should throw DatabaseException when product does not exist"() {
        given:
        productRepository.findById(1) >> Optional.ofNullable(null)
        when:
        productService.getById(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should return object's collection when products exist"() {
        given:
        Product product1 = Mock(Product.class)
        Product product2 = Mock(Product.class)
        and:
        productRepository.findAll() >> List.of(product1, product2)
        when:
        List<Product> returnedProducts = productService.getAll()
        then:
        returnedProducts.size() == 2
        returnedProducts.contains(product1)
        returnedProducts.contains(product2)
    }

    def "Should throw DatabaseException when repository findAll throws Exception"() {
        given:
        productRepository.findAll() >> { throw new RuntimeException() }
        when:
        productService.getAll()
        then:
        thrown(DatabaseException.class)
    }

    def "Should not throw exception when product deleted"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Product product = Product.builder()
                .bakery(bakery)
                .build()
        User user = User.builder()
                .bakery(bakery)
                .build()
        AuthUserDetails authUserDetails = new AuthUserDetails(user)
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(authUserDetails, null, null)
        and:
        productRepository.findById(1) >> Optional.of(product)
        and:
        productRepository.deleteById(1)
        when:
        productService.delete(1, token)
        then:
        noExceptionThrown()
    }

    def "Should throw AccessDeniedException when product deleted and product does not belong to manager bakery"() {
        given:
        Bakery bakery1 = Bakery.builder()
                .id(1)
                .build()
        Bakery bakery2 = Bakery.builder()
                .id(2)
                .build()
        Product product = Product.builder()
                .bakery(bakery1)
                .build()
        User user = User.builder()
                .bakery(bakery2)
                .build()
        AuthUserDetails authUserDetails = new AuthUserDetails(user)
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(authUserDetails, null, null)
        and:
        productRepository.findById(1) >> Optional.of(product)
        and:
        productRepository.deleteById(1)
        when:
        productService.delete(1, token)
        then:
        thrown(AccessDeniedException.class)
    }

    def "Should throw DatabaseException when repository deleteById throws Exception"() {
        given:
        productRepository.deleteById(1) >> { throw new RuntimeException() }
        when:
        productService.delete(1, null)
        then:
        thrown(DatabaseException.class)
    }

    def "Should return saved object when product has been successfully updated"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Category category = Category.builder()
                .id(1)
                .build()
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        List<ProductAvailability> productAvailabilities = List.of(ProductAvailability.builder()
                .id(1)
                .build())
        and:
        this.bakeryService.getById(_ as Integer) >> bakery
        this.categoryService.getById(_ as Integer) >> category
        this.productTypeService.getById(_ as Integer) >> productType
        this.productAvailabilityRepository.findAllByProduct(_ as Product) >> productAvailabilities
        and:
        Product product = Product.builder()
                .id(1)
                .bakery(bakery)
                .category(category)
                .productType(productType)
                .productAvailability(productAvailabilities)
                .build()
        and:
        productRepository.save(product) >> product
        when:
        Product returnedProduct = this.productService.update(product)
        then:
        returnedProduct == product
    }

    def "Should update throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Category category = Category.builder()
                .id(1)
                .build()
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        List<ProductAvailability> productAvailabilities = List.of(ProductAvailability.builder()
                .id(1)
                .build())
        and:
        this.bakeryService.getById(_ as Integer) >> bakery
        this.categoryService.getById(_ as Integer) >> category
        this.productTypeService.getById(_ as Integer) >> productType
        this.productAvailabilityRepository.findAllByProduct(_ as Product) >> productAvailabilities
        and:
        Product product = Product.builder()
                .id(1)
                .bakery(bakery)
                .category(category)
                .productType(productType)
                .productAvailability(productAvailabilities)
                .build()
        and:
        productRepository.save(_ as Product) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.productService.update(product)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Category category = Category.builder()
                .id(1)
                .build()
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        List<ProductAvailability> productAvailabilities = List.of(ProductAvailability.builder()
                .id(1)
                .build())
        and:
        this.bakeryService.getById(_ as Integer) >> bakery
        this.categoryService.getById(_ as Integer) >> category
        this.productTypeService.getById(_ as Integer) >> productType
        this.productAvailabilityRepository.findAllByProduct(_ as Product) >> productAvailabilities
        and:
        Product product = Product.builder()
                .id(1)
                .bakery(bakery)
                .category(category)
                .productType(productType)
                .productAvailability(productAvailabilities)
                .build()
        and:
        productRepository.save(_ as Product) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.productService.update(product)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw DatabaseException when repository save throws exception"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Category category = Category.builder()
                .id(1)
                .build()
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        List<ProductAvailability> productAvailabilities = List.of(ProductAvailability.builder()
                .id(1)
                .build())
        and:
        this.bakeryService.getById(_ as Integer) >> bakery
        this.categoryService.getById(_ as Integer) >> category
        this.productTypeService.getById(_ as Integer) >> productType
        this.productAvailabilityRepository.findAllByProduct(_ as Product) >> productAvailabilities
        and:
        Product product = Product.builder()
                .id(1)
                .bakery(bakery)
                .category(category)
                .productType(productType)
                .productAvailability(productAvailabilities)
                .build()
        and:
        productRepository.save(_ as Product) >> { p -> throw new RuntimeException("message") }
        when:
        this.productService.update(product)
        then:
        thrown(DatabaseException.class)
    }

    def "Should add return saved object when product has been successfully added"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Category category = Category.builder()
                .id(1)
                .build()
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        and:
        this.bakeryService.getById(_ as Integer) >> bakery
        this.categoryService.getById(_ as Integer) >> category
        this.productTypeService.getById(_ as Integer) >> productType
        and:
        Product product = Product.builder()
                .id(1)
                .bakery(bakery)
                .category(category)
                .productType(productType)
                .build()
        and:
        productRepository.save(product) >> product
        when:
        Product returnedProduct = this.productService.add(product)
        then:
        returnedProduct == product
    }

    def "Should add throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Category category = Category.builder()
                .id(1)
                .build()
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        and:
        this.bakeryService.getById(_ as Integer) >> bakery
        this.categoryService.getById(_ as Integer) >> category
        this.productTypeService.getById(_ as Integer) >> productType
        and:
        Product product = Product.builder()
                .id(1)
                .bakery(bakery)
                .category(category)
                .productType(productType)
                .build()
        and:
        productRepository.save(_ as Product) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.productService.add(product)
        then:
        thrown(ConstraintException.class)
    }

    def "Should add throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Category category = Category.builder()
                .id(1)
                .build()
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        and:
        this.bakeryService.getById(_ as Integer) >> bakery
        this.categoryService.getById(_ as Integer) >> category
        this.productTypeService.getById(_ as Integer) >> productType
        and:
        Product product = Product.builder()
                .id(1)
                .bakery(bakery)
                .category(category)
                .productType(productType)
                .build()
        and:
        productRepository.save(_ as Product) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.productService.add(product)
        then:
        thrown(ConstraintException.class)
    }

    def "Should add throw DatabaseException when repository save throws exception"() {
        given:
        Bakery bakery = Bakery.builder()
                .id(1)
                .build()
        Category category = Category.builder()
                .id(1)
                .build()
        ProductType productType = ProductType.builder()
                .id(1)
                .build()
        List<ProductAvailability> productAvailabilities = List.of(ProductAvailability.builder()
                .id(1)
                .build())
        and:
        this.bakeryService.getById(_ as Integer) >> bakery
        this.categoryService.getById(_ as Integer) >> category
        this.productTypeService.getById(_ as Integer) >> productType
        this.productAvailabilityRepository.findAllByProduct(_ as Product) >> productAvailabilities
        and:
        Product product = Product.builder()
                .id(1)
                .bakery(bakery)
                .category(category)
                .productType(productType)
                .productAvailability(productAvailabilities)
                .build()
        and:
        productRepository.save(_ as Product) >> { p -> throw new RuntimeException("message") }
        when:
        this.productService.add(product)
        then:
        thrown(DatabaseException.class)
    }

    def "Should getByBakery return product's collection with given bakery"() {
        given:
        Bakery bakery1 = Bakery.builder().id(1).build()
        Product product1 = Product.builder().bakery(bakery1).build()
        Bakery bakery2 = Bakery.builder().id(2).build()
        Product product2 = Product.builder().bakery(bakery2).build()
        and:
        productRepository.findAll() >> List.of(product1, product2)
        when:
        List<Product> returnedProducts = this.productService.getByBakery(1)
        then:
        returnedProducts.size() == 1
        returnedProducts.first() == product1
    }

    def "Should getByBakery throw DatabaseException when repository thrown exception"() {
        given:
        productRepository.findAll() >> { throw new RuntimeException() }
        when:
        this.productService.getByBakery(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should getByCategory return product's collection with given category"() {
        given:
        Category category1 = Category.builder().id(1).build()
        Product product1 = Product.builder().category(category1).build()
        Category category2 = Category.builder().id(2).build()
        Product product2 = Product.builder().category(category2).build()
        and:
        productRepository.findAll() >> List.of(product1, product2)
        when:
        List<Product> returnedProducts = this.productService.getByCategory(1)
        then:
        returnedProducts.size() == 1
        returnedProducts.first() == product1
    }

    def "Should getByCategory throw DatabaseException when repository thrown exception"() {
        given:
        productRepository.findAll() >> { throw new RuntimeException() }
        when:
        this.productService.getByCategory(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should getByCategoryAndBakery return product's collection with given bakery and category"() {
        given:
        Category category1 = Category.builder().id(1).build()
        Bakery bakery1 = Bakery.builder().id(1).build()
        Product product1 = Product.builder().category(category1).bakery(bakery1).build()
        Category category2 = Category.builder().id(2).build()
        Bakery bakery2 = Bakery.builder().id(2).build()
        Product product2 = Product.builder().category(category2).bakery(bakery2).build()
        and:
        productRepository.findAll() >> List.of(product1, product2)
        when:
        List<Product> returnedProducts = this.productService.getByCategoryAndBakery(1, 1)
        then:
        returnedProducts.size() == 1
        returnedProducts.first() == product1
    }

    def "Should getByCategoryAndBakery throw DatabaseException when repository thrown exception"() {
        given:
        productRepository.findAll() >> { throw new RuntimeException() }
        when:
        this.productService.getByCategoryAndBakery(1, 1)
        then:
        thrown(DatabaseException.class)
    }
}
