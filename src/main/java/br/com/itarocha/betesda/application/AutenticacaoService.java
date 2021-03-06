package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.dto.RedefinicaoSenha;
import br.com.itarocha.betesda.adapter.out.email.EmailService;
import br.com.itarocha.betesda.application.out.RoleRepository;
import br.com.itarocha.betesda.application.out.UserRepository;
import br.com.itarocha.betesda.application.out.UserTokenRepository;
import br.com.itarocha.betesda.application.port.in.AutenticacaoUseCase;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import br.com.itarocha.betesda.domain.enums.RoleNameEnum;
import br.com.itarocha.betesda.util.validacoes.EntityValidationException;
import br.com.itarocha.betesda.util.validacoes.Violation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements AutenticacaoUseCase {

	private final UserRepository userRepository;
	private final UserTokenRepository userTokenRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	@Override
	@Transactional
	public User create(UserToSave userToSave) {
		Set<Violation> violations = new HashSet<>();

		if (userRepository.existsByUsername(userToSave.getUsername())) {
			violations.add(new Violation("username", "Nome de usuário já cadastrado!"));
		}

		if (userRepository.existsByEmail(userToSave.getEmail())) {
			violations.add(new Violation("email", "Email já está em uso!"));
		}

		if (!violations.isEmpty()){
			throw new EntityValidationException(violations);
		}

		Role userRole = roleRepository.findByName(RoleNameEnum.ROLE_USER)
				.orElseThrow(() -> new IllegalArgumentException("Role não existe"));

		String passwordEncoded = passwordEncoder.encode(userToSave.getPassword());
		User user = User.builder()
				.name(userToSave.getName())
				.username(userToSave.getUsername())
				.email(userToSave.getEmail())
				.password(passwordEncoded)
				.roles(Collections.singleton(userRole))
				.build();

		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void solicitarEnvioSenha(String email) {
		/*
		this.$route.path
		https://stackoverflow.com/questions/40382388/how-to-set-url-query-params-in-vue-with-vue-router
		routes: [
		{ name: 'user-view', path: '/user/:id', component: UserView },
		// other routes
		]
		this.$router.replace({ name: "user-view", params: {id:"123"}, query: {q1: "q1"} })
		*/

		Set<Violation> violations = new HashSet<>();

		Optional<User> optUser  = userRepository.findByEmail(email);

		if (optUser.isEmpty()) {
			violations.add(new Violation("email", "Email não encontrado"));
		}
		if (!violations.isEmpty()){
			throw new EntityValidationException(violations);
		}

		// Excluir todos os tokens desse email
		userTokenRepository.deleteAllByEmail(email);

		String token = UUID.randomUUID().toString();
		LocalDateTime agora = LocalDateTime.now();
		LocalDateTime validade = agora.plusHours(6);

		UserToken ut = UserToken.builder()
				.email(email)
				.token(token)
				.ativo(LogicoEnum.S)
				.dataHoraCriacao(agora)
				.dataHoraValidade(validade)
				.build();

		userTokenRepository.save(ut);

		User user = optUser.get();
		emailService.solicitarSenha(user.getEmail(), user.getName(), token);
	}

	@Override
	@Transactional
	public boolean redefinirSenha(RedefinicaoSenha request) {
		Set<Violation> violations = new HashSet<>();

		if (!request.getSenha().equals(request.getSenhaConfirmacao())) {
			violations.add(new Violation("senha", "Senha de confirmação diferente da senha informada"));
		}

		if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
			violations.add(new Violation("email", "Email não cadastrado"));
		}

		List<UserToken> lstUser  = userTokenRepository.findByEmailAndToken(request.getEmail(), request.getToken());
		if (lstUser.isEmpty()) {
			violations.add(new Violation("email", "Combinação email e token não encontrados"));
		}

		LocalDateTime agora = LocalDateTime.now();
		lstUser.stream().forEach(userToken -> {
			if (agora.isAfter(userToken.getDataHoraValidade())) {
				violations.add(new Violation("token", "Token fora de validade"));
			}
		});

		if (!violations.isEmpty()){
			throw new EntityValidationException(violations);
		}

		String newPassword = passwordEncoder.encode(request.getSenha());
		userTokenRepository.updatePassword(request.getEmail(), newPassword);

		userTokenRepository.deleteAllByEmail(request.getEmail());

		return true;
	}
}
