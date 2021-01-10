package br.com.itarocha.betesda.util.validation;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class EntityValidationError {

	@Builder.Default
	private List<FieldValidationError> errors = new ArrayList<>();
	
	public EntityValidationError addError(String fieldName, String message) {
		this.errors.add(new FieldValidationError(fieldName, message));
		return this;
	}

	public boolean hasErrors(){
		return !this.errors.isEmpty();
	}
}
