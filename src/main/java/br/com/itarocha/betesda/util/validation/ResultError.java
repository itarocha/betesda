package br.com.itarocha.betesda.util.validation;

import java.util.ArrayList;
import java.util.List;

public class ResultError {
	
	private List<FieldValidationError> errors;
	
	public ResultError() {
		this.errors = new ArrayList<>();
	}

	public List<FieldValidationError> getErrors() {
		return errors;
	}

	public void setErrors(List<FieldValidationError> errors) {
		this.errors = errors;
	}
	
	public ResultError addError(String fieldName, String message) {
		this.errors.add(new FieldValidationError(fieldName, message));
		return this;
	}

	public boolean hasErrors(){
		return this.errors.size() > 0;
	}
}
