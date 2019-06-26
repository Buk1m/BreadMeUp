package com.pkkl.BreadMeUp.services

import com.pkkl.BreadMeUp.clients.GoogleMapsClient
import com.pkkl.BreadMeUp.exceptions.ConstraintException
import com.pkkl.BreadMeUp.exceptions.DatabaseException
import com.pkkl.BreadMeUp.model.Bakery
import com.pkkl.BreadMeUp.repositories.BakeryRepository
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

class BakeryServiceTest extends Specification {

    private BakeryRepository bakeryRepository = Mock(BakeryRepository.class)

    private GoogleMapsClient googleMapsClient = Mock(GoogleMapsClient.class)

    private BakeryService bakeryService

    def setup() {
        this.bakeryService = new BakeryServiceImpl(bakeryRepository, googleMapsClient)
    }

    def "Should return object when bakery exists"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.findById(1) >> Optional.of(bakery)
        when:
        Bakery returnedBakery = bakeryService.getById(1)
        then:
        returnedBakery == bakery
    }

    def "Should throw DatabaseException when bakery does not exist"() {
        given:
        bakeryRepository.findById(1) >> Optional.ofNullable(null)
        when:
        bakeryService.getById(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should return object's collection when bakeries exist"() {
        given:
        Bakery bakery1 = Mock(Bakery.class)
        Bakery bakery2 = Mock(Bakery.class)
        and:
        bakeryRepository.findAll() >> List.of(bakery1, bakery2)
        when:
        List<Bakery> returnedBakeries = bakeryService.getAll()
        then:
        returnedBakeries.size() == 2
        returnedBakeries.contains(bakery1)
        returnedBakeries.contains(bakery2)
    }

    def "Should throw DatabaseException when repository findAll throws Exception"() {
        given:
        bakeryRepository.findAll() >> { throw new RuntimeException() }
        when:
        bakeryService.getAll()
        then:
        thrown(DatabaseException.class)
    }

    def "Should not throw exception when bakery deleted"() {
        given:
        bakeryRepository.deleteById(1)
        when:
        bakeryService.delete(1)
        then:
        noExceptionThrown()
    }

    def "Should throw DatabaseException when repository deleteById throws Exception"() {
        given:
        bakeryRepository.deleteById(1) >> { throw new RuntimeException() }
        when:
        bakeryService.delete(1)
        then:
        thrown(DatabaseException.class)
    }

    def "Should return saved object when bakery has been successfully updated"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.save(bakery) >> bakery
        when:
        Bakery returnedBakery = this.bakeryService.update(bakery)
        then:
        returnedBakery == bakery
    }

    def "Should update throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.save(_ as Bakery) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.bakeryService.update(bakery)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.save(_ as Bakery) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.bakeryService.update(bakery)
        then:
        thrown(ConstraintException.class)
    }

    def "Should update throw DatabaseException when repository save throws exception"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.save(_ as Bakery) >> { p -> throw new RuntimeException("message") }
        when:
        this.bakeryService.update(bakery)
        then:
        thrown(DatabaseException.class)
    }

    def "Should add return saved object when bakery has been successfully added"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.save(bakery) >> bakery
        when:
        Bakery returnedBakery = this.bakeryService.add(bakery)
        then:
        returnedBakery == bakery
    }

    def "Should add throw ConstraintException when repository save throws ConstraintViolationException"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.save(_ as Bakery) >> { p -> throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>()) }
        when:
        this.bakeryService.add(bakery)
        then:
        thrown(ConstraintException.class)
    }

    def "Should add throw ConstraintException when repository save throws hibernate ConstraintViolationException"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.save(_ as Bakery) >> { p -> throw new RuntimeException(new org.hibernate.exception.ConstraintViolationException(null, null, null)) }
        when:
        this.bakeryService.add(bakery)
        then:
        thrown(ConstraintException.class)
    }

    def "Should add throw DatabaseException when repository save throws exception"() {
        given:
        Bakery bakery = Mock(Bakery.class)
        and:
        bakeryRepository.save(_ as Bakery) >> { p -> throw new RuntimeException("message") }
        when:
        this.bakeryService.add(bakery)
        then:
        thrown(DatabaseException.class)
    }
}
