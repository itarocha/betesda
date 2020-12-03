package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class DestinacaoHospedagemMapper {

    public DestinacaoHospedagem toModel(DestinacaoHospedagemEntity entity){
        if (isNull(entity)) return null;

        return DestinacaoHospedagem.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .build();
    }

    public DestinacaoHospedagemEntity toEntity(DestinacaoHospedagem model){
        if (isNull(model)) return null;

        return DestinacaoHospedagemEntity.builder()
                .id(model.getId())
                .descricao(model.getDescricao())
                .build();
    }

    public SelectValueVO toSelectValueVO(DestinacaoHospedagem input){
        return new SelectValueVO(input.getId(), input.getDescricao());
    }

}
