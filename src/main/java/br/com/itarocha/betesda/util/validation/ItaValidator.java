package br.com.itarocha.betesda.util.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ItaValidator<T> {
	
	private T ref;
	private ResultError re;

	public ItaValidator(T ref) {
		this.ref = ref;
		this.re = new ResultError();
	}
	
	public void validate() {
		this.re.getErrors().clear();
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(this.ref);
		for (ConstraintViolation<T> violation : violations ) {
			this.re.addError(violation.getPropertyPath().toString(), violation.getMessage());
			//this.re.getErrors().add(new FieldValidationError(violation.getPropertyPath().toString(), violation.getMessage()));
		}
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
}
