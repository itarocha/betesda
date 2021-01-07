package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.QuartoEntity;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.Leito;
import br.com.itarocha.betesda.domain.Quarto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class QuartoMapper {

    private final DestinacaoHospedagemMapper destinacaoHospedagemMapper;
    private final LeitoMapper leitoMapper;

    public Quarto toModel(QuartoEntity entity) {
        if (isNull(entity)) return null;

        Set<DestinacaoHospedagem> destinacoes = isNull(entity.getDestinacoes()) ? new HashSet<>()
                :  entity.getDestinacoes()
                .stream()
                .map(destinacaoHospedagemMapper::toModel)
                .collect(Collectors.toSet());

        List<Leito> leitos = isNull(entity.getLeitos()) ? new ArrayList<>()
                : entity.getLeitos()
                .stream()
                .map(leitoMapper::toModel)
                .collect(Collectors.toList());

        return Quarto.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .descricao(entity.getDescricao())
                .displayText(entity.getDisplayText())
                .destinacoes(destinacoes)
                .leitos(leitos)
                .build();
    }

    public QuartoEntity toEntity(Quarto model) {
        if (isNull(model)) return null;

        Set<DestinacaoHospedagemEntity> destinacoes = isNull(model.getDestinacoes()) ? new HashSet<>()
                : model.getDestinacoes()
                .stream()
                .map(destinacaoHospedagemMapper::toEntity)
                .collect(Collectors.toSet());

        return QuartoEntity.builder()
                .id(model.getId())
                .numero(model.getNumero())
                .descricao(model.getDescricao())
                .ativo(model.getAtivo())
                .destinacoes(destinacoes)
                //.leitos() ?????????????? // TODO REVER
                .build();
    }

}