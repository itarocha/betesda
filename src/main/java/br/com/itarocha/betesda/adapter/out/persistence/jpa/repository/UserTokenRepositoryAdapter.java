package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.UserTokenEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.UsuarioTokenMapper;
import br.com.itarocha.betesda.core.ports.out.UserTokenRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTokenRepositoryAdapter implements UserTokenRepository {

    private final UserTokenJpaRepository repository;
    private final UsuarioTokenMapper mapper;

    @Override
    public UserToken save(UserToken model) {
        try {
            return mapper.toModel(repository.save(mapper.toEntity(model)));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar UsuarioToken"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<UserToken> findById(Long id) {
        Optional<UserTokenEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public Optional<UserToken> findByToken(String token) {
        Optional<UserTokenEntity> opt = repository.findByToken(token);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public List<UserToken> findAllByEmail(String email) {
        return repository.findByEmail(email).stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void delete(UserToken model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir UsuarioToken"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public void deleteAllByEmail(String email) {
        try {
            repository.deleteAllWhereEmail(email);
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir UsuarioToken"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public List<UserToken> findByEmailAndToken(String email, String token) {
        return repository.findByEmailAndToken(email, token).stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        repository.updatePassword(email, newPassword);
    }

}
