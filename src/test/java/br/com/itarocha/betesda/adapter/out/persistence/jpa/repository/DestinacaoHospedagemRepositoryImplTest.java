package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.DestinacaoHospedagemRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.DestinacaoHospedagemMapper;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
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
class DestinacaoHospedagemRepositoryImplTest {

    @Autowired
    private DestinacaoHospedagemJpaRepository repository;

    private DestinacaoHospedagemRepositoryImpl repoAdapter;

    @Test
    @DisplayName("Ao persistir DestinacaoHospedagem retorna sucesso")
    void save_PersistDestinacaoHospedagem_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new DestinacaoHospedagemRepositoryImpl(repository, new DestinacaoHospedagemMapper());
        assertThat(repoAdapter).isNotNull();
        DestinacaoHospedagem toSave =  makeDestinacaoHospedagemMockSucesso();

        DestinacaoHospedagem saved = repoAdapter.save(toSave);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescricao()).isEqualTo(toSave.getDescricao());
    }

    @Test
    @DisplayName("Ao gravar DestinacaoHospedagem com valores inválidos, retorna ConstraintValidationException")
    void save_PersistDestinacaoHospedagem_WhenConstraintValidation() {
        assertThat(repository).isNotNull();

        repoAdapter = new DestinacaoHospedagemRepositoryImpl(repository, new DestinacaoHospedagemMapper());
        assertThat(repoAdapter).isNotNull();
        DestinacaoHospedagem toSave = makeDestinacaoHospedagemMockInvalido();

        assertThrows(ConstraintViolationException.class, () -> repoAdapter.save(toSave));
    }

    @Test
    @DisplayName("Ao executar findById em DestinacaoHospedagem retorna sucesso")
    void findById_PersistDestinacaoHospedagem_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new DestinacaoHospedagemRepositoryImpl(repository, new DestinacaoHospedagemMapper());
        assertThat(repoAdapter).isNotNull();
        DestinacaoHospedagem toSave = makeDestinacaoHospedagemMockSucesso();

        DestinacaoHospedagem saved = repoAdapter.save(toSave);
        Optional<DestinacaoHospedagem> opt = repoAdapter.findById(saved.getId());

        assertTrue(opt.isPresent());
        assertThat(opt.get()).isNotNull();
        assertThat(opt.get().getId()).isNotNull();
        assertThat(opt.get().getDescricao()).isEqualTo(toSave.getDescricao());
    }

    @Test
    @DisplayName("Ao executar findByAllOrderByDescricao em DestinacaoHospedagem retorna sucesso")
    void findAllOrderByDescricao_DestinacaoHospedagem_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new DestinacaoHospedagemRepositoryImpl(repository, new DestinacaoHospedagemMapper());

        String[] nomes = {"GGGGGG", "AAAAAA", "ZZZZZZ", "BBBBBB"};

        Arrays.stream(nomes).collect(Collectors.toList()).forEach(s -> {
            repoAdapter.save(DestinacaoHospedagem.builder().descricao(s).build());
        });

        List<DestinacaoHospedagem> lista = repoAdapter.findAllOrderByDescricao();
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
    @DisplayName("Ao executar delete em DestinacaoHospedagem retorna sucesso")
    void delete_DestinacaoHospedagem_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new DestinacaoHospedagemRepositoryImpl(repository, new DestinacaoHospedagemMapper());
        assertThat(repoAdapter).isNotNull();
        DestinacaoHospedagem toSave = makeDestinacaoHospedagemMockSucesso();

        DestinacaoHospedagem saved = repoAdapter.save(toSave);
        Optional<DestinacaoHospedagem> opt = repoAdapter.findById(saved.getId());
        assertTrue(opt.isPresent());

        repoAdapter.delete(saved);
        Optional<DestinacaoHospedagem> optEmpty = repoAdapter.findById(saved.getId());

        assertTrue(optEmpty.isEmpty());
    }

    private DestinacaoHospedagem makeDestinacaoHospedagemMockSucesso(){
        return DestinacaoHospedagem.builder()
                .descricao("TESTE")
                .build();
    }

    private DestinacaoHospedagem makeDestinacaoHospedagemMockInvalido(){
        return DestinacaoHospedagem.builder()
                .build();
    }
}