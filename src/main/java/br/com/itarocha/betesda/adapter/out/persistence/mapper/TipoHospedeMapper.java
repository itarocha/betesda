package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.adapter.out.persistence.entity.TipoHospedeEntity;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoHospede;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class TipoHospedeMapper {

    public TipoHospede toModel(TipoHospedeEntity entity){
        if (isNull(entity)) return null;

        return TipoHospede.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .build();
    }

    public TipoHospedeEntity toEntity(TipoHospede model){
        if (isNull(model)) return null;

        return TipoHospedeEntity.builder()
                .id(model.getId())
                .descricao(model.getDescricao())
                .build();
    }

    public SelectValueVO toSelectValueVO(TipoHospede input){
        return new SelectValueVO(input.getId(), input.getDescricao());
    }

}
