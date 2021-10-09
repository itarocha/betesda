package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.SituacaoLeitoEntity;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import br.com.itarocha.betesda.domain.ItemDictionary;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class SituacaoLeitoMapper {

    public SituacaoLeito toModel(SituacaoLeitoEntity entity) {
        if (isNull(entity)) return null;

        return SituacaoLeito.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .disponivel(entity.getDisponivel())
                .build();
    }

    public SituacaoLeitoEntity toEntity(SituacaoLeito model) {
        if (isNull(model)) return null;

        return SituacaoLeitoEntity.builder()
                .id(model.getId())
                .descricao(model.getDescricao())
                .disponivel(model.getDisponivel())
                .build();
    }

    public ItemDictionary toSelectValueVO(SituacaoLeito input){
        return new ItemDictionary(input.getId(), input.getDescricao());
    }

}
