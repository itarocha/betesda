package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.SituacaoLeitoRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.SituacaoLeitoMapper;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class SituacaoLeitoRepositoryImplTest {

    @Autowired
    private SituacaoLeitoJpaRepository repository;

    private SituacaoLeitoRepositoryImpl repoAdapter;

    @Test
    @DisplayName("Ao persistir SituacaoLeito retorna sucesso")
    public void save_PersistSituacaoLeito_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new SituacaoLeitoRepositoryImpl(repository, new SituacaoLeitoMapper());
        assertThat(repoAdapter).isNotNull();
        SituacaoLeito toSave =  makeSituacaoLeitoMockSucesso();

        SituacaoLeito saved = repoAdapter.save(toSave);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescricao()).isEqualTo(toSave.getDescricao());
        assertThat(saved.getDisponivel()).isEqualTo(toSave.getDisponivel());
    }

    @Test
    @DisplayName("Ao gravar SituacaoLeito com valores inválidos, retorna ConstraintValidationException")
    public void save_PersistSituacaoLeito_WhenConstraintValidation() {
        assertThat(repository).isNotNull();

        repoAdapter = new SituacaoLeitoRepositoryImpl(repository, new SituacaoLeitoMapper());
        assertThat(repoAdapter).isNotNull();
        SituacaoLeito toSave = makeSituacaoLeitoMockInvalido();

        assertThrows(ConstraintViolationException.class, () -> repoAdapter.save(toSave));
    }

    @Test
    @DisplayName("Ao executar findById em SituacaoLeito retorna sucesso")
    public void findById_PersistSituacaoLeito_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new SituacaoLeitoRepositoryImpl(repository, new SituacaoLeitoMapper());
        assertThat(repoAdapter).isNotNull();
        SituacaoLeito toSave = makeSituacaoLeitoMockSucesso();

        SituacaoLeito saved = repoAdapter.save(toSave);
        Optional<SituacaoLeito> opt = repoAdapter.findById(saved.getId());

        assertTrue(opt.isPresent());
        assertThat(opt.get()).isNotNull();
        assertThat(opt.get().getId()).isNotNull();
        assertThat(opt.get().getDescricao()).isEqualTo(toSave.getDescricao());
        assertThat(saved.getDisponivel()).isEqualTo(toSave.getDisponivel());
    }

    @Test
    @DisplayName("Ao executar findByAllOrderByDescricao em SituacaoLeito retorna sucesso")
    public void findAllOrderByDescricao_SituacaoLeito_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new SituacaoLeitoRepositoryImpl(repository, new SituacaoLeitoMapper());

        String[] nomes = {"GGGGGG", "AAAAAA", "ZZZZZZ", "BBBBBB"};

        Arrays.stream(nomes).collect(Collectors.toList()).forEach(s -> {
            repoAdapter.save(SituacaoLeito.builder().descricao(s).disponivel(LogicoEnum.S).build());
        });

        List<SituacaoLeito> lista = repoAdapter.findAllOrderByDescricao();
        assertThat(lista).isNotNull();
        assertThat(lista.size()).isEqualTo(nomes.length);

        StringBuilder sbData = new StringBuilder();
        lista.stream().forEach(ts -> sbData.append(ts.getDescricao()));

        StringBuilder sbSorted = new StringBuilder();
        Arrays.stream(nomes).sorted().forEach(s -> sbSorted.append(s));

        assertThat(sbSorted.toString()).isEqualTo(sbData.toString());

        List<ItemDictionary> lstSelect = repoAdapter.findAllToSelectVO();
        assertThat(lstSelect).isNotNull();
        assertThat(lstSelect.size()).isEqualTo(nomes.length);
    }

    @Test
    @DisplayName("Ao executar delete em SituacaoLeito retorna sucesso")
    public void delete_SituacaoLeito_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new SituacaoLeitoRepositoryImpl(repository, new SituacaoLeitoMapper());
        assertThat(repoAdapter).isNotNull();
        SituacaoLeito toSave = makeSituacaoLeitoMockSucesso();

        SituacaoLeito saved = repoAdapter.save(toSave);
        Optional<SituacaoLeito> opt = repoAdapter.findById(saved.getId());
        assertTrue(opt.isPresent());

        repoAdapter.delete(saved);
        Optional<SituacaoLeito> optEmpty = repoAdapter.findById(saved.getId());

        assertTrue(optEmpty.isEmpty());
    }

    private SituacaoLeito makeSituacaoLeitoMockSucesso(){
        return SituacaoLeito.builder()
                .descricao("TESTE")
                .disponivel(LogicoEnum.S)
                .build();
    }

    private SituacaoLeito makeSituacaoLeitoMockInvalido(){
        return SituacaoLeito.builder()
                .build();
    }
}