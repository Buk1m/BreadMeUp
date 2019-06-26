package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.clients.GoogleMapsClient;
import com.pkkl.BreadMeUp.dtos.BakeryLocationDto;
import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.exceptions.NotFoundException;
import com.pkkl.BreadMeUp.model.Bakery;
import com.pkkl.BreadMeUp.repositories.BakeryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Slf4j

@PropertySource("classpath:google.properties")
@Service
public class BakeryServiceImpl implements BakeryService {

    private final BakeryRepository bakeryRepository;

    private final GoogleMapsClient googleMapsClient;

    @Value("${key}")
    private String key;

    @Autowired
    public BakeryServiceImpl(BakeryRepository bakeryRepository, GoogleMapsClient googleMapsClient) {
        this.bakeryRepository = bakeryRepository;
        this.googleMapsClient = googleMapsClient;
    }

    @Override
    public Bakery getById(final int id) {
        try {
            return bakeryRepository.findById(id).orElseThrow(() -> new RuntimeException("Bakery doesn't exist \\U+1F635"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Bakery> getAll() {
        try {
            return bakeryRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(final int id) {
        try {
            bakeryRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public Bakery update(final Bakery bakery) {
        return saveOrUpdate(bakery);
    }

    @Override
    public Bakery add(final Bakery bakery) {
        return saveOrUpdate(bakery);
    }

    @Override
    public BakeryLocationDto getLocation(int id) {
        try {
            Bakery bakery = this.bakeryRepository.findById(id).orElseThrow(() -> {
                throw new NotFoundException("Bakery does not exist");
            });
            return this.googleMapsClient.getGoogle(bakery.getPlaceId(), key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DatabaseException(e);
        }
    }

    private Bakery saveOrUpdate(final Bakery bakery) {
        try {
            return bakeryRepository.save(bakery);
        } catch (ConstraintViolationException e) {
            log.error(e.getMessage(), e);
            throw new ConstraintException(e.getConstraintViolations().toString(), e);
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                log.error(e.getMessage(), e);
                throw new ConstraintException(e.getMessage(), e);
            }
            log.error(e.getMessage(), e);
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
