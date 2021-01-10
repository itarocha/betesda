package br.com.itarocha.betesda.util.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ItaValidator<T> {
	
	private T ref;
	private EntityValidationError re;

	public ItaValidator(T ref) {
		this.ref = ref;
		this.re = EntityValidationError.builder().build();
	}
	
	public EntityValidationError validate() {
		this.re.getErrors().clear();
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(this.ref);
		for (ConstraintViolation<T> violation : violations ) {
			this.re.addError(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return this.re;
	}

	public Optional<EntityValidationError> newValidate() {
		this.re.getErrors().clear();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(this.ref);
		for (ConstraintViolation<T> violation : violations ) {
			this.re.addError(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return hasErrors() ? Optional.empty() : Optional.of(this.re);
	}


	public boolean hasErrors() {
		return this.re.getErrors().isEmpty();
	}
	
	public EntityValidationError getValidationResult() {
		return this.re;
	}

	public void addError(String fieldName, String message) {
		this.re.addError(fieldName, message);
	}

	public Function<T, EntityValidationError> teste = t -> {
		validate();
		return  getValidationResult();
	};
}
