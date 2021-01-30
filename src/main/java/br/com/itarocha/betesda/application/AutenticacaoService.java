package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.out.RoleRepository;
import br.com.itarocha.betesda.application.out.UserRepository;
import br.com.itarocha.betesda.application.port.in.AutenticacaoUseCase;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.domain.enums.RoleNameEnum;
import br.com.itarocha.betesda.util.validacoes.EntityValidationException;
import br.com.itarocha.betesda.util.validacoes.Violation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements AutenticacaoUseCase {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
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

	/*
	@Override
	public TipoLeito create(TipoLeito model) {
		return repository.save(model);
	}
	*/

	/*
	@Override
	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}

	@Override
	public TipoLeito update(TipoLeito model) {
		Optional<TipoLeito> result = repository.findById(model.getId());
		return result.isPresent() ? repository.save(result.get()) : null;
	}

	@Override
  	public TipoLeito find(Long id) {
		Optional<TipoLeito> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	@Override
	public List<TipoLeito> findAll() {
		return repository.findAllOrderByDescricao();
	}

	@Override
	public List<SelectValueVO> listSelect() {
		return repository.findAllToSelectVO();
	}
	*/
}
