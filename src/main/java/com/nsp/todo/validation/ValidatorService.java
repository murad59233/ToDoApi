package com.nsp.todo.validation;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.ValidationException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

@Service
public class ValidatorService {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    @SneakyThrows
    public <T> void validate(T t, Class<?>... clazz){
        Set<String> errors = new HashSet<>();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t,clazz);
        if(violations.size()==0)
            return;
        violations.stream().forEach(e->errors.add(e.getPropertyPath().toString() + " is not valid"));
        throw new ValidationException(Status.VALIDATION_ERROR,errors);
    }

}
