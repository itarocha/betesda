package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.RoleEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.RoleJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.RoleMapper;
import br.com.itarocha.betesda.core.ports.out.RoleRepository;
import br.com.itarocha.betesda.domain.Role;
import br.com.itarocha.betesda.domain.enums.RoleNameEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository repository;
    private final RoleMapper mapper;

    @Override
    public Optional<Role> findByName(RoleNameEnum roleName) {
        Optional<RoleEntity> opt = repository.findByName(roleName);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }
}