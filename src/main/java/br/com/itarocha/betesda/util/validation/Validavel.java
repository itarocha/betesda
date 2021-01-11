package br.com.itarocha.betesda.util.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class Validavel<T> {

    private final T value;
    private boolean isValidated = false;

    private EntityValidationError validador = EntityValidationError.builder().build();

    public Validavel(T t){
        this.value = Objects.requireNonNull(t);
    }

    public Validavel<T> isValid(Consumer<T> acao){
        validador = validate(this.value);
        if (!validador.hasErrors()) {
            acao.accept(this.value);
        }
        isValidated = true;
        return this;
    }

    public void orElse(Consumer<EntityValidationError> errorConsumer){
        if (!isValidated){
            validador = validate(this.value);
        }
        if (validador.hasErrors()){
            errorConsumer.accept(validador);
        }
    }

    private EntityValidationError validate(T model){
        EntityValidationError result = EntityValidationError.builder().build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        for (ConstraintViolation<T> violation : violations ) {
            result.addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return result;
    }

}
