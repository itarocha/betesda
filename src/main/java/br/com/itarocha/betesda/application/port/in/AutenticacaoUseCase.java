package br.com.itarocha.betesda.application.port.in;

import br.com.itarocha.betesda.domain.UserToSave;
import br.com.itarocha.betesda.domain.User;

public interface AutenticacaoUseCase {

	User create(UserToSave model);
}
