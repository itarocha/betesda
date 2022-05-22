package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoServicoEntity;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoServico;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class TipoServicoMapper {

    public TipoServico toModel(TipoServicoEntity entity) {
        if (isNull(entity)) return null;

        return TipoServico.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    public TipoServicoEntity toEntity(TipoServico model) {
        if (isNull(model)) return null;

        return TipoServicoEntity.builder()
                .id(model.getId())
                .descricao(model.getDescricao())
                .ativo(model.getAtivo())
                .build();
    }

    public ItemDictionary toSelectValueVO(TipoServico input){
        return new ItemDictionary(input.getId(), input.getDescricao());
    }

}
