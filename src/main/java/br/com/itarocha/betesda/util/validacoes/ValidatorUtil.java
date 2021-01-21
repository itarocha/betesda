package br.com.itarocha.betesda.util.validacoes;

import org.springframework.stereotype.Component;

import javax.validation.*;
import java.util.HashSet;
import java.util.Set;

@Component
public class ValidatorUtil {

    public void validate(Object obj) throws ValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Object>> constraints = validator.validate(obj);
        Set<Violation> violations = new HashSet<>();

        constraints.stream().forEach(v -> {
            violations.add(new Violation(String.valueOf(v.getPropertyPath()), v.getMessage() ));
        });

        if (!violations.isEmpty()){
            throw new ValidationException(violations);
        }
    }

}
