package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.RoleEntity;
import br.com.itarocha.betesda.domain.Role;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class RoleMapper {

    public Role toModel(RoleEntity entity){
        if (isNull(entity)) { return null; }

        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public RoleEntity toEntity(Role model){
        if (isNull(model)) { return null; }

        return RoleEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

}
