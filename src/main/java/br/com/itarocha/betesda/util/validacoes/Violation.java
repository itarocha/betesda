package br.com.itarocha.betesda.util.validacoes;

public class Violation {

	private String fieldName;
	private String message;

	public Violation(String fieldName, String message) {
		this.fieldName = fieldName;
		this.message = message;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public String getMessage() {
		return message;
	}

}
