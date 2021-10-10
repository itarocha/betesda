package br.com.itarocha.betesda.domain.mapper;

import br.com.itarocha.betesda.domain.hospedagem.RelatorioGeral;
import br.com.itarocha.betesda.report.PessoaAtendida;
import br.com.itarocha.betesda.report.ResumoHospedagem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class RelatorioGeralMapper {

    public PessoaAtendida convertToPessoaAtendida(RelatorioGeral rg, LocalDate dataInicial) {
        PessoaAtendida pessoa = new PessoaAtendida();
        pessoa.setId(rg.getPessoaId());
        pessoa.setNome(rg.getPessoaNome());
        pessoa.setDataNascimento(rg.getPessoaDataNascimento());
        int idade = Period.between(rg.getPessoaDataNascimento(), dataInicial).getYears();
        pessoa.setIdade(idade);
        pessoa.setCidade(rg.getCidade());
        pessoa.setUf(rg.getUf());
        pessoa.setCidadeOrigem(rg.getCidade().trim().concat(" - ").concat(rg.getUf()));
        return pessoa;
    }

    public ResumoHospedagem convertRelatorioGeral(RelatorioGeral model){
        ResumoHospedagem resumo = new ResumoHospedagem();
        resumo.setHospedagemId(model.getHospedagemId());
        resumo.setHospedeId(model.getHospedeId());
        resumo.setLeitoId(model.getLeitoId());
        resumo.setDataIni(model.getDataIni());
        resumo.setDataFim(model.getDataFim());
        resumo.setDias(model.getDias());
        resumo.setTipoUtilizacao(model.getTipoUtilizacao());
        resumo.setTipoUtilizacaoDescricao("T".equals(model.getTipoUtilizacao()) ? "Total" : "Parcial");
        resumo.setTipoHospedeId(model.getTipoHospedeId());
        resumo.setTipoHospedeDescricao(model.getTipoHospedeDescricao());
        resumo.setEntidadeId(model.getEntidadeId());
        resumo.setEntidadeNome(model.getEntidadeNome());
        resumo.setPessoaId(model.getPessoaId());
        return resumo;
    }

}
