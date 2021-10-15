package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.TipoServicoRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoServicoMapper;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoServico;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TipoServicoRepositoryImplTest {

    @Autowired
    private TipoServicoJpaRepository repository;

    private TipoServicoRepositoryImpl repoAdapter;

    @Test
    @DisplayName("Ao persistir TipoServico retorna sucesso")
    void save_PersistTipoServico_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoServicoRepositoryImpl(repository, new TipoServicoMapper());
        assertThat(repoAdapter).isNotNull();
        TipoServico toSave =  makeTipoServicoMockSucesso();

        TipoServico saved = repoAdapter.save(toSave);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescricao()).isEqualTo(toSave.getDescricao());
        assertThat(saved.getAtivo()).isEqualTo(toSave.getAtivo());
    }

    @Test
    @DisplayName("Ao gravar TipoServico com valores inválidos, retorna ConstraintValidationException")
    void save_PersistTipoServico_WhenConstraintValidation() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoServicoRepositoryImpl(repository, new TipoServicoMapper());
        assertThat(repoAdapter).isNotNull();
        TipoServico toSave = makeTipoServicoMockInvalido();

        assertThrows(ConstraintViolationException.class, () -> repoAdapter.save(toSave));
    }

    @Test
    @DisplayName("Ao executar findById em TipoServico retorna sucesso")
    void findById_PersistTipoServico_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoServicoRepositoryImpl(repository, new TipoServicoMapper());
        assertThat(repoAdapter).isNotNull();
        TipoServico toSave = makeTipoServicoMockSucesso();

        TipoServico saved = repoAdapter.save(toSave);
        Optional<TipoServico> opt = repoAdapter.findById(saved.getId());

        assertTrue(opt.isPresent());
        assertThat(opt.get()).isNotNull();
        assertThat(opt.get().getId()).isNotNull();
        assertThat(opt.get().getDescricao()).isEqualTo(toSave.getDescricao());
        assertThat(saved.getAtivo()).isEqualTo(opt.get().getAtivo());
    }

    @Test
    @DisplayName("Ao executar findByAllOrderByDescricao em TipoServico retorna sucesso")
    void findAllOrderByDescricao_TipoServico_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoServicoRepositoryImpl(repository, new TipoServicoMapper());

        String[] nomesAtivos = {"GGGGGG", "AAAAAA", "ZZZZZZ", "BBBBBB"};
        String[] nomesInativos = {"VVVVVV", "CCCCCC", "EEEEEE"};
        String[] todos = ArrayUtils.addAll(nomesAtivos, nomesInativos);

        Arrays.stream(nomesAtivos).collect(Collectors.toList()).forEach(s -> {
            repoAdapter.save(TipoServico.builder().descricao(s).build());
        });
        Arrays.stream(nomesInativos).collect(Collectors.toList()).forEach(s -> {
            repoAdapter.save(TipoServico.builder().descricao(s).ativo(LogicoEnum.N).build());
        });

        List<TipoServico> lista = repoAdapter.findAllOrderByDescricao();
        assertThat(lista).isNotNull();
        assertThat(lista.size()).isEqualTo(todos.length);

        StringBuilder sbData = new StringBuilder();
        lista.stream().forEach(ts -> sbData.append(ts.getDescricao()));

        StringBuilder sbSorted = new StringBuilder();
        Arrays.stream(todos).sorted().forEach(s -> sbSorted.append(s));

        assertThat(sbSorted.toString()).isEqualTo(sbData.toString());

        List<ItemDictionary> lstSelect = repoAdapter.findAllToSelectVO();
        assertThat(lstSelect).isNotNull();
        assertThat(lstSelect.size()).isEqualTo(nomesAtivos.length);

        List<TipoServico> lstAtivos = repoAdapter.findAllAtivosOrderByDescricao();
        assertThat(lstSelect).isNotNull();
        assertThat(lstSelect.size()).isEqualTo(nomesAtivos.length);
    }

    @Test
    @DisplayName("Ao executar delete em TipoServico retorna sucesso")
    void delete_TipoServico_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoServicoRepositoryImpl(repository, new TipoServicoMapper());
        assertThat(repoAdapter).isNotNull();
        TipoServico toSave = makeTipoServicoMockSucesso();

        TipoServico saved = repoAdapter.save(toSave);
        Optional<TipoServico> opt = repoAdapter.findById(saved.getId());
        assertTrue(opt.isPresent());

        repoAdapter.delete(saved);
        Optional<TipoServico> optEmpty = repoAdapter.findById(saved.getId());

        assertTrue(optEmpty.isEmpty());
    }

    private TipoServico makeTipoServicoMockSucesso(){
        return TipoServico.builder()
                .descricao("TESTE")
                .ativo(LogicoEnum.S)
                .build();
    }

    private TipoServico makeTipoServicoMockInvalido(){
        return TipoServico.builder()
                .ativo(LogicoEnum.S)
                .build();
    }
}