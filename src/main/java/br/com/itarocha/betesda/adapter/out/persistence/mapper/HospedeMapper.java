package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeLeitoEntity;
import br.com.itarocha.betesda.domain.Hospede;
import br.com.itarocha.betesda.domain.HospedeLeito;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class HospedeMapper {

    private final PessoaMapper pessoaMapper;
    private final TipoHospedeMapper tipoHospedeMapper;
    private final HospedeLeitoMapper hospedeLeitoMapper;

    public Hospede toModel(HospedeEntity entity) {
        if (isNull(entity)) return null;

        List<HospedeLeito> leitos = isNull(entity.getLeitos()) ? new ArrayList<>()
                : entity.getLeitos()
                .stream()
                .map(hospedeLeitoMapper::toModel)
                .collect(Collectors.toList());

        return Hospede.builder()
                .id(entity.getId())
                .pessoa(pessoaMapper.toModel(entity.getPessoa()))
                .tipoHospede(tipoHospedeMapper.toModel(entity.getTipoHospede()))
                .baixado(entity.getBaixado())
                .leitos(leitos)
                .build();
    }

    public HospedeEntity toEntity(Hospede model) {
        if (isNull(model)) return null;

        List<HospedeLeitoEntity> leitos = isNull(model.getLeitos()) ? new ArrayList<>()
                : model.getLeitos()
                .stream()
                .map(hospedeLeitoMapper::toEntity)
                .collect(Collectors.toList());

        return HospedeEntity.builder()
                .id(model.getId())
                .pessoa(pessoaMapper.toEntity(model.getPessoa()))
                .tipoHospede(tipoHospedeMapper.toEntity(model.getTipoHospede()))
                .baixado(model.getBaixado())
                .leitos(leitos)
                .build();
    }

}