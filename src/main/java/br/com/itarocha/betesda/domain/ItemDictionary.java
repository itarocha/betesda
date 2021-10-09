package br.com.itarocha.betesda.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDictionary implements Serializable{
	private static final long serialVersionUID = -432146171536143461L;
	private Long value;
	private String text;
}
