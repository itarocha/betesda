package br.com.itarocha.betesda.util.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.function.BiFunction;

public class StaticValidator {

    public static <T> ResultError validate(T model){
        ResultError result = new ResultError();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        for (ConstraintViolation<T> violation : violations ) {
            result.addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return result;
    }

    public static <T> boolean instanceIsValid(T model){
        ResultError result = new ResultError();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        for (ConstraintViolation<T> violation : violations ) {
            result.addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return result.hasErrors();
    }


    //BIPREDICATE
    // T, U, R
    class BiFunctionDemo implements BiFunction<Integer, Integer, Integer>{

        @Override
        public Integer apply(Integer t, Integer u) {
            return null;
        }
    }

}
