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
	private ResultError re;

	public ItaValidator(T ref) {
		this.ref = ref;
		this.re = new ResultError();
	}
	
	public ResultError validate() {
		this.re.getErrors().clear();
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(this.ref);
		for (ConstraintViolation<T> violation : violations ) {
			this.re.addError(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return this.re;
	}

	public Optional<ResultError> newValidate() {
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
	
	public ResultError getErrors() {
		return this.re;
	}

	public void addError(String fieldName, String message) {
		this.re.addError(fieldName, message);
	}

	public Function<T, ResultError> teste = t -> {
		validate();
		return  getErrors();
	};
}
