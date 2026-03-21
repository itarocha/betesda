package br.com.itarocha.betesda.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectValueVO implements Serializable{

	private static final long serialVersionUID = -432146171536143461L;

	private Long value;
	
	private String text;
}