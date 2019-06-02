package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import org.springframework.data.repository.CrudRepository;

import javax.validation.ConstraintViolationException;

@SuppressWarnings("unchecked")
class BaseService<T, R extends CrudRepository> {

    protected R repository;

    T saveOrUpdate(T item) {
        try {
            return (T) repository.save(item);
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
