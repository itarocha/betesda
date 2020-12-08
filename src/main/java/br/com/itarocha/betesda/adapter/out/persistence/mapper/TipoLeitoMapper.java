package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.entity.SituacaoLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.entity.TipoLeitoEntity;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import br.com.itarocha.betesda.domain.TipoLeito;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class TipoLeitoMapper {

    public TipoLeito toModel(TipoLeitoEntity entity) {
        if (isNull(entity)) return null;

        return TipoLeito.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .quantidadeCamas(entity.getQuantidadeCamas())
                .build();
    }

    public TipoLeitoEntity toEntity(TipoLeito model) {
        if (isNull(model)) return null;

        return TipoLeitoEntity.builder()
                .id(model.getId())
                .descricao(model.getDescricao())
                .quantidadeCamas(model.getQuantidadeCamas())
                .build();
    }

    public SelectValueVO toSelectValueVO(TipoLeito input){
        return new SelectValueVO(input.getId(), input.getDescricao());
    }

}
