package br.com.itarocha.betesda.core.ports.in;

//TODO: Não pode chamar **.adapter.**
import br.com.itarocha.betesda.adapter.dto.RedefinicaoSenha;
import br.com.itarocha.betesda.domain.UserToSave;
import br.com.itarocha.betesda.domain.User;

public interface AutenticacaoUseCase {
	User create(UserToSave model);
	void solicitarEnvioSenha(String email);
	boolean redefinirSenha(RedefinicaoSenha request);
}
