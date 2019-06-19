package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.Bakery;
import com.pkkl.BreadMeUp.repositories.BakeryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Service
public class BakeryServiceImpl implements BakeryService {
    private final BakeryRepository bakeryRepository;

    @Autowired
    public BakeryServiceImpl(BakeryRepository bakeryRepository) {
        this.bakeryRepository = bakeryRepository;
    }

    @Override
    public Bakery getById(final int id) {
        try {
            return bakeryRepository.findById(id).orElseThrow(() -> new RuntimeException("Bakery doesn't exist \\U+1F635"));
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Bakery> getAll() {
        try {
            return bakeryRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(final int id) {
        try {
            bakeryRepository.deleteById(id);
        } catch (Exception e) {
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

    private Bakery saveOrUpdate(final Bakery bakery) {
        try {
            return bakeryRepository.save(bakery);
        } catch (ConstraintViolationException e) {
            throw new ConstraintException(e.getConstraintViolations().toString(), e);
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new ConstraintException(e.getMessage(), e);
            }
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
