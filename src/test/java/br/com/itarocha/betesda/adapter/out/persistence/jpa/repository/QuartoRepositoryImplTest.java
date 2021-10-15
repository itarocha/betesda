package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.DestinacaoHospedagemRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.QuartoRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.*;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.Quarto;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuartoRepositoryImplTest {

    private static final String QUARTO_PREFIX = "QUARTO_";
    private static final String DESTINACAO_PREFIX = "DESTINACAO_";

    @Autowired
    private QuartoJpaRepository repository;

    @Autowired
    private DestinacaoHospedagemJpaRepository destinacaoRepository;

    @Test
    @DisplayName("Teste de injeção")
    public void testAdapter() {
        assertThat(repository).isNotNull();
        assertThat(destinacaoRepository).isNotNull();
    }

    @Test
    @DisplayName("Ao persistir Quarto retorna sucesso")
    public void save_PersistQuarto_WhenSuccessful() {
        QuartoRepositoryImpl repoAdapter = buildRepoAdapter();
        DestinacaoHospedagemRepositoryImpl repoDestinacoes = buildDestinacaoHospedagemAdapter();
        Set<DestinacaoHospedagem> destinacoes = buildDestinacoesMock(repoDestinacoes,3);

        List<Quarto> quartos = buildQuartosMock(repoAdapter, 3, destinacoes);

        Quarto saved = repoAdapter.save(quartos.get(0));

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescricao()).isEqualTo(QUARTO_PREFIX+1);
        assertThat(saved.getDestinacoes()).isNotNull();
        assertThat(saved.getDestinacoes().size()).isEqualTo(3);

        assertThat(repoAdapter.existeOutroQuartoComEsseNumero(1)).isNotNull();
        assertThat(repoAdapter.existeOutroQuartoComEsseNumero(2)).isNotNull();
        assertThat(repoAdapter.existeOutroQuartoComEsseNumero(3)).isNotNull();
        assertThat(repoAdapter.existeOutroQuartoComEsseNumero(4)).isNullOrEmpty();
    }

    private DestinacaoHospedagemRepositoryImpl buildDestinacaoHospedagemAdapter() {
        return new DestinacaoHospedagemRepositoryImpl(destinacaoRepository,
                new DestinacaoHospedagemMapper());
    }

    private Set<DestinacaoHospedagem> buildDestinacoesMock(DestinacaoHospedagemRepositoryImpl repo, Integer qtd){
        Set<DestinacaoHospedagem> destinacoes = new HashSet<>();
        IntStream.rangeClosed(1, qtd).boxed().forEach(x ->
            destinacoes.add(repo.save(DestinacaoHospedagem.builder().descricao(DESTINACAO_PREFIX+x).build()))
        );
        return destinacoes;
    }

    private List<Quarto> buildQuartosMock(QuartoRepositoryImpl repo, Integer qtd, Set<DestinacaoHospedagem> destinacoes){
        List<Quarto> quartos = new ArrayList<>();
        IntStream.rangeClosed(1, qtd).boxed().forEach(x ->
                quartos.add(repo.save(Quarto.builder()
                        .descricao(QUARTO_PREFIX+x)
                        .numero(x)
                        .ativo(LogicoEnum.S)
                        .destinacoes(destinacoes)
                        .build()))
        );
        return quartos;
    }

    private QuartoRepositoryImpl buildRepoAdapter() {
        DestinacaoHospedagemMapper destinacaoHospedagemMapper = new DestinacaoHospedagemMapper();
        TipoLeitoMapper tipoLeitoMapper = new TipoLeitoMapper();
        SituacaoLeitoMapper situacaoLeitoMapper = new SituacaoLeitoMapper();
        QuartoMapper quartoMapper = new QuartoMapper(destinacaoHospedagemMapper);
        LeitoMapper leitoMapper = new LeitoMapper(tipoLeitoMapper, situacaoLeitoMapper, quartoMapper);
        return new QuartoRepositoryImpl(repository, new QuartoMapper(destinacaoHospedagemMapper));
    }
}