package br.com.itarocha.betesda.exception;

import br.com.itarocha.betesda.util.validation.EntityValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//////////@ResponseStatus(HttpStatus.BAD_REQUEST)
@Deprecated
public class ObsoleteValidationException extends Exception{

	private static final long serialVersionUID = -1173120804319840769L;

	private EntityValidationError re;
	
	public ObsoleteValidationException(EntityValidationError re) {
		this.re = re;
	}

	public EntityValidationError getRe() {
		return re;
	}

	public void setRe(EntityValidationError re) {
		this.re = re;
	}
	
}