package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.UserEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.UserJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.UserMapper;
import br.com.itarocha.betesda.core.ports.out.UserRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    //private final UserJpaRepository repository;
    private final UserMapper mapper;

    //TODO REVER
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(null);
        //Optional<UserEntity> opt = repository.findById(id);
        //return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    //TODO REVER
    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(null);
        /*
        Optional<UserEntity> opt = repository.findByEmail(email);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
         */
    }

    //TODO REVER
    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return Optional.ofNullable(null);
        /*
        Optional<UserEntity> opt = repository.findByUsernameOrEmail(username, email);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
         */
    }

    //TODO REVER
    @Override
    public List<User> findByIdIn(List<Long> userIds) {
        return new ArrayList<>();
        /*
        return repository.findByIdIn(userIds).stream().map(mapper::toModel).collect(Collectors.toList());
         */
    }

    //TODO REVER
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(null);
        /*
        Optional<UserEntity> opt = repository.findByUsername(username);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
         */
    }

    @Override
    public Boolean existsByUsername(String username) {
        return true;
        //return repository.existsByUsername(username);
    }

    //TODO REVER
    @Override
    public Boolean existsByEmail(String email) {
        return false;
        //return repository.existsByEmail(email);
    }

    //TODO REVER
    @Override
    public User save(User model) {
        return null;
        /*
        try {
            return mapper.toModel(repository.save(mapper.toEntity(model)));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Tipo de Serviço"
                    , e.getMostSpecificCause().getMessage());
        }
         */
    }

    //TODO REVER
    @Override
    public void delete(User model) {
        /*
        try {
            repository.delete(mapper.toEntity(model));
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Tipo de Serviço"
                    , e.getMostSpecificCause().getMessage());
        }
         */
    }

}
