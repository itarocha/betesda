package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.RoleEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.UserEntity;
import br.com.itarocha.betesda.domain.Role;
import br.com.itarocha.betesda.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class UserMapper {

    public User toModel(UserEntity entity){
        if (isNull(entity)) { return null; }

        Set<Role> roles = isNull(entity.getRoles()) ? new HashSet<>() : entity.getRoles()
                .stream()
                .map(this::roleToModel)
                .collect(Collectors.toSet());

        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .roles(roles)
                .build();
    }

    public UserEntity toEntity(User model){
        if (isNull(model)) { return null; }

        Set<RoleEntity> roles = isNull(model.getRoles()) ? new HashSet<>() : model.getRoles()
                .stream()
                .map(this::roleModelToEntity)
                .collect(Collectors.toSet());

        return UserEntity.builder()
                .id(model.getId())
                .email(model.getEmail())
                .name(model.getName())
                .username(model.getUsername())
                .password(model.getPassword())
                .roles(roles)
                .build();
    }

    private Role roleToModel(RoleEntity entity){
        if (isNull(entity)) { return null; }

        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    private RoleEntity roleModelToEntity(Role model){
        if (isNull(model)) { return null; }

        return RoleEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

}
