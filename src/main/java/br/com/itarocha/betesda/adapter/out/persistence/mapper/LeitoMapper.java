package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.LeitoEntity;
import br.com.itarocha.betesda.domain.Leito;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class LeitoMapper {

    private final TipoLeitoMapper tipoLeitoMapper;
    private final SituacaoLeitoMapper situacaoLeitoMapper;
    private final QuartoMapper quartoMapper;

    public Leito toModel(LeitoEntity entity) {
        if (isNull(entity)) return null;

        return Leito.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .tipoLeito(tipoLeitoMapper.toModel(entity.getTipoLeito()))
                .situacao(situacaoLeitoMapper.toModel(entity.getSituacao()))
                .quarto(quartoMapper.toModel(entity.getQuarto()))
                .build();
    }

    public LeitoEntity toEntity(Leito model) {
        if (isNull(model)) return null;

        return LeitoEntity.builder()
                .id(model.getId())
                .numero(model.getNumero())
                .tipoLeito(tipoLeitoMapper.toEntity(model.getTipoLeito()))
                .situacao(situacaoLeitoMapper.toEntity(model.getSituacao()))
                .build();
    }

}
