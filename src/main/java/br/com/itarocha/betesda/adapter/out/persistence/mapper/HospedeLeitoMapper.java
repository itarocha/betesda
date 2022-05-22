package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeLeitoEntity;
import br.com.itarocha.betesda.domain.HospedeLeito;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class HospedeLeitoMapper {

    private final QuartoMapper quartoMapper;
    private final LeitoMapper leitoMapper;
    //private final HospedeMapper hospedeMapper;

    public HospedeLeito toModel(HospedeLeitoEntity entity) {
        if (isNull(entity)) return null;

        return HospedeLeito.builder()
                .id(entity.getId())
                //.hospede()
                .quarto(quartoMapper.toModel(entity.getQuarto()))
                .leito(leitoMapper.toModel(entity.getLeito()))
                .dataEntrada(entity.getDataEntrada())
                .dataSaida(entity.getDataSaida())
                .build();
    }

    public HospedeLeitoEntity toEntity(HospedeLeito model) {
        if (isNull(model)) return null;

        return HospedeLeitoEntity.builder()
                .id(model.getId())
                //.hospede()
                .quarto(quartoMapper.toEntity(model.getQuarto()))
                .leito(leitoMapper.toEntity(model.getLeito()))
                .dataEntrada(model.getDataEntrada())
                .dataSaida(model.getDataSaida())
                .build();
    }

}