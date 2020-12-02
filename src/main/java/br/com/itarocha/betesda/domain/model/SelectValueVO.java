package br.com.itarocha.betesda.domain.model;

import java.io.Serializable;

public class SelectValueVO implements Serializable{

	private static final long serialVersionUID = -432146171536143461L;

	private Long value;
	
	private String text;

	public Long getValue() {
		return value;
	}

	public SelectValueVO() {
		
	}
	
	public SelectValueVO(Long value, String text) {
		this.value = value;
		this.text = text;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
