package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.TipoHospedeRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoHospedeMapper;
import br.com.itarocha.betesda.domain.TipoHospede;
import br.com.itarocha.betesda.domain.ItemDictionary;
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
public class TipoHospedeRepositoryImplTest {

    @Autowired
    private TipoHospedeJpaRepository repository;

    private TipoHospedeRepositoryImpl repoAdapter;

    @Test
    @DisplayName("Ao persistir TipoHospede retorna sucesso")
    public void save_PersistTipoHospede_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoHospedeRepositoryImpl(repository, new TipoHospedeMapper());
        assertThat(repoAdapter).isNotNull();
        TipoHospede toSave =  makeTipoHospedeMockSucesso();

        TipoHospede saved = repoAdapter.save(toSave);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescricao()).isEqualTo(toSave.getDescricao());
    }

    @Test
    @DisplayName("Ao gravar TipoHospede com valores inválidos, retorna ConstraintValidationException")
    public void save_PersistTipoHospede_WhenConstraintValidation() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoHospedeRepositoryImpl(repository, new TipoHospedeMapper());
        assertThat(repoAdapter).isNotNull();
        TipoHospede toSave = makeTipoHospedeMockInvalido();

        assertThrows(ConstraintViolationException.class, () -> repoAdapter.save(toSave));
    }

    @Test
    @DisplayName("Ao executar findById em TipoHospede retorna sucesso")
    public void findById_PersistTipoHospede_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoHospedeRepositoryImpl(repository, new TipoHospedeMapper());
        assertThat(repoAdapter).isNotNull();
        TipoHospede toSave = makeTipoHospedeMockSucesso();

        TipoHospede saved = repoAdapter.save(toSave);
        Optional<TipoHospede> opt = repoAdapter.findById(saved.getId());

        assertTrue(opt.isPresent());
        assertThat(opt.get()).isNotNull();
        assertThat(opt.get().getId()).isNotNull();
        assertThat(opt.get().getDescricao()).isEqualTo(toSave.getDescricao());
    }

    @Test
    @DisplayName("Ao executar findByAllOrderByDescricao em TipoHospede retorna sucesso")
    public void findAllOrderByDescricao_TipoHospede_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoHospedeRepositoryImpl(repository, new TipoHospedeMapper());

        String[] nomes = {"GGGGGG", "AAAAAA", "ZZZZZZ", "BBBBBB"};

        Arrays.stream(nomes).collect(Collectors.toList()).forEach(s -> {
            repoAdapter.save(TipoHospede.builder().descricao(s).build());
        });

        List<TipoHospede> lista = repoAdapter.findAllOrderByDescricao();
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
    @DisplayName("Ao executar delete em TipoHospede retorna sucesso")
    public void delete_TipoHospede_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoHospedeRepositoryImpl(repository, new TipoHospedeMapper());
        assertThat(repoAdapter).isNotNull();
        TipoHospede toSave = makeTipoHospedeMockSucesso();

        TipoHospede saved = repoAdapter.save(toSave);
        Optional<TipoHospede> opt = repoAdapter.findById(saved.getId());
        assertTrue(opt.isPresent());

        repoAdapter.delete(saved);
        Optional<TipoHospede> optEmpty = repoAdapter.findById(saved.getId());

        assertTrue(optEmpty.isEmpty());
    }

    private TipoHospede makeTipoHospedeMockSucesso(){
        return TipoHospede.builder()
                .descricao("TESTE")
                .build();
    }

    private TipoHospede makeTipoHospedeMockInvalido(){
        return TipoHospede.builder()
                .build();
    }
}