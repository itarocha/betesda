package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.UserTokenEntity;
import br.com.itarocha.betesda.domain.UserToken;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class UsuarioTokenMapper {

    public UserToken toModel(UserTokenEntity entity) {
        if (isNull(entity)) return null;

        return UserToken.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .token(entity.getToken())
                .dataHoraCriacao(entity.getDataHoraCriacao())
                .dataHoraValidade(entity.getDataHoraValidade())
                .ativo(entity.getAtivo())
                .build();
    }

    public UserTokenEntity toEntity(UserToken model) {
        if (isNull(model)) return null;

        return UserTokenEntity.builder()
                .id(model.getId())
                .email(model.getEmail())
                .token(model.getToken())
                .dataHoraCriacao(model.getDataHoraCriacao())
                .dataHoraValidade(model.getDataHoraValidade())
                .ativo(model.getAtivo())
                .build();
    }

}
