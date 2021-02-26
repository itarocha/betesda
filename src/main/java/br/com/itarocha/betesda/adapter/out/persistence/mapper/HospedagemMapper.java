package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedagemEntity;
import br.com.itarocha.betesda.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class HospedagemMapper {

    private final EntidadeMapper entidadeMapper;
    private final DestinacaoHospedagemMapper destinacaoHospedagemMapper;
    private final EncaminhadorMapper encaminhadorMapper;
    private final TipoServicoMapper tipoServicoMapper;
    private final HospedeMapper hospedeMapper;

    private final LeitoMapper leitoMapper;

    public Hospedagem toModel(HospedagemEntity entity){
        if (isNull(entity)) return null;

        List<TipoServico> servicos = isNull(entity.getServicos()) ? new ArrayList<>()
                :  entity.getServicos()
                .stream()
                .map(ts -> tipoServicoMapper.toModel(ts.getTipoServico()))
                .collect(Collectors.toList());

        List<Hospede> hospedes = isNull(entity.getHospedes()) ? new ArrayList<>()
                :  entity.getHospedes()
                .stream()
                .map(hospedeMapper::toModel)
                .collect(Collectors.toList());

        return Hospedagem.builder()
                .id(entity.getId())
                .entidade(entidadeMapper.toModel(entity.getEntidade()))
                .encaminhador(encaminhadorMapper.toModel(entity.getEncaminhador()))
                .destinacaoHospedagem(destinacaoHospedagemMapper.toModel(entity.getDestinacaoHospedagem()))
                .dataEntrada(entity.getDataEntrada())
                .dataPrevistaSaida(entity.getDataPrevistaSaida())
                .dataEfetivaSaida(entity.getDataEfetivaSaida())
                .tipoUtilizacao(entity.getTipoUtilizacao())
                .observacoes(entity.getObservacoes())
                //.status(entity.getCe)
                .hospedes(hospedes)
                .servicos(servicos)
                .build();
    }
}