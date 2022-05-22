package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.UserTokenEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.UserTokenJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.UsuarioTokenMapper;
import br.com.itarocha.betesda.core.ports.out.UserTokenRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTokenRepositoryImpl implements UserTokenRepository {

    private final UsuarioTokenMapper mapper;

    @Autowired
    UserTokenJpaRepository repository;

    @Override
    public UserToken save(UserToken model) {
        return null;
        /*
        try {
            return repository.save(model);
            //return mapper.toModel(repository.save(model));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar UsuarioToken"
                    , e.getMostSpecificCause().getMessage());
        }
         */
    }

    @Override
    public Optional<UserToken> findById(Long id) {
        return Optional.ofNullable(null);
        //return repository.findById(id);
    }

    @Override
    public Optional<UserToken> findByToken(String token) {
        return Optional.ofNullable(null);
        //return repository.findByToken(token);
    }

    @Override
    public List<UserToken> findAllByEmail(String email) {
        return new ArrayList<>();
        //return repository.findAllByEmail(email).stream().collect(Collectors.toList());
    }

    @Override
    public void delete(UserToken model) {
        /*
        try {
            repository.delete(model);
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir UsuarioToken"
                    , e.getMostSpecificCause().getMessage());
        }
         */
    }

    @Override
    public void deleteAllByEmail(String email) {
        /*
        try {
            repository.deleteAllByEmail(email);
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir UsuarioToken"
                    , e.getMostSpecificCause().getMessage());
        }
         */
    }

    @Override
    public List<UserToken> findByEmailAndToken(String email, String token) {
        return new ArrayList<>();
        //return repository.findByEmailAndToken(email, token).stream().collect(Collectors.toList());
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        repository.updatePassword(email, newPassword);
    }

}
