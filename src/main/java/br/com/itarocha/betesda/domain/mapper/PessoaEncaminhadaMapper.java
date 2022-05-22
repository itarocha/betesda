package br.com.itarocha.betesda.domain.mapper;

import br.com.itarocha.betesda.domain.hospedagem.PessoaEncaminhada;
import br.com.itarocha.betesda.report.PessoaEncaminhamento;
import org.springframework.stereotype.Component;

@Component
public class PessoaEncaminhadaMapper {

    public PessoaEncaminhamento convertEncaminhamento(PessoaEncaminhada entrada){
        PessoaEncaminhamento pe = new PessoaEncaminhamento();
        pe.setHospedagemId(entrada.getHospedagemId());
        pe.setPessoaId(entrada.getPessoaId());
        pe.setTipoHospedeId(entrada.getTipoHospedeId());
        pe.setTipoHospedeDescricao(entrada.getTipoHospedeDescricao());
        pe.setTipoUtilizacao(entrada.getTipoUtilizacao());
        pe.setDestinacaoHospedagemId(entrada.getDestinacaoHospedagemId());
        pe.setDestinacaoHospedagemDescricao(entrada.getDestinacaoHospedagemDescricao());
        pe.setEntidadeId(entrada.getEntidadeId());
        pe.setEntidadeNome(entrada.getEntidadeNome());
        return pe;
    }

}
