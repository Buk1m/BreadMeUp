package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

@Slf4j

@SuppressWarnings("unchecked")
class BaseService<T, R extends CrudRepository> {

    protected R repository;

    @Transactional
    T saveOrUpdate(T item) {
        try {
            return (T) repository.save(item);
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
