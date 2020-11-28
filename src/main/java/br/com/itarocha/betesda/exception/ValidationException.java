package br.com.itarocha.betesda.exception;

import br.com.itarocha.betesda.util.validation.ResultError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends Exception{

	private static final long serialVersionUID = -1173120804319840769L;

	private ResultError re;
	
	public ValidationException(ResultError re) {
		this.re = re;
	}

	public ResultError getRe() {
		return re;
	}

	public void setRe(ResultError re) {
		this.re = re;
	}
	
}
