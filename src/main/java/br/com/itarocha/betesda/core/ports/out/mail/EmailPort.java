package br.com.itarocha.betesda.core.ports.out.mail;

import br.com.itarocha.betesda.core.domain.model.Email;

public interface EmailPort {

	void redefinirSenha(String emailDestinatario, String nome, String token);
	void send(Email eParams);

}
