package br.com.itarocha.betesda.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchRequest {
	
	private String fieldName;
	
	private String value;
	
	private String operator;
}