package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoLeitoMapper;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoLeito;
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
public class TipoLeitoRepositoryAdapterTest {

    @Autowired
    private TipoLeitoJpaRepository repository;

    private TipoLeitoRepositoryAdapter repoAdapter;

    @Test
    @DisplayName("Ao persistir TipoLeito retorna sucesso")
    public void save_PersistTipoLeito_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoLeitoRepositoryAdapter(repository, new TipoLeitoMapper());
        assertThat(repoAdapter).isNotNull();
        TipoLeito toSave =  makeTipoLeitoMockSucesso();

        TipoLeito saved = repoAdapter.save(toSave);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescricao()).isEqualTo(toSave.getDescricao());
        assertThat(saved.getQuantidadeCamas()).isEqualTo(toSave.getQuantidadeCamas());
    }

    @Test
    @DisplayName("Ao gravar TipoLeito com valores inválidos, retorna ConstraintValidationException")
    public void save_PersistTipoLeito_WhenConstraintValidation() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoLeitoRepositoryAdapter(repository, new TipoLeitoMapper());
        assertThat(repoAdapter).isNotNull();
        TipoLeito toSave = makeTipoLeitoMockInvalido();

        assertThrows(ConstraintViolationException.class, () -> repoAdapter.save(toSave));
    }

    @Test
    @DisplayName("Ao executar findById em TipoLeito retorna sucesso")
    public void findById_PersistTipoLeito_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoLeitoRepositoryAdapter(repository, new TipoLeitoMapper());
        assertThat(repoAdapter).isNotNull();
        TipoLeito toSave = makeTipoLeitoMockSucesso();

        TipoLeito saved = repoAdapter.save(toSave);
        Optional<TipoLeito> opt = repoAdapter.findById(saved.getId());

        assertTrue(opt.isPresent());
        assertThat(opt.get()).isNotNull();
        assertThat(opt.get().getId()).isNotNull();
        assertThat(opt.get().getDescricao()).isEqualTo(toSave.getDescricao());
        assertThat(saved.getQuantidadeCamas()).isEqualTo(toSave.getQuantidadeCamas());
    }

    @Test
    @DisplayName("Ao executar findByAllOrderByDescricao em TipoLeito retorna sucesso")
    public void findAllOrderByDescricao_TipoLeito_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoLeitoRepositoryAdapter(repository, new TipoLeitoMapper());

        String[] nomes = {"GGGGGG", "AAAAAA", "ZZZZZZ", "BBBBBB"};

        Arrays.stream(nomes).collect(Collectors.toList()).forEach(s -> {
            repoAdapter.save(TipoLeito.builder().descricao(s).quantidadeCamas(1).build());
        });

        List<TipoLeito> lista = repoAdapter.findAllOrderByDescricao();
        assertThat(lista).isNotNull();
        assertThat(lista.size()).isEqualTo(nomes.length);

        StringBuilder sbData = new StringBuilder();
        lista.stream().forEach(ts -> sbData.append(ts.getDescricao()));

        StringBuilder sbSorted = new StringBuilder();
        Arrays.stream(nomes).sorted().forEach(s -> sbSorted.append(s));

        assertThat(sbSorted.toString()).isEqualTo(sbData.toString());

        List<SelectValueVO> lstSelect = repoAdapter.findAllToSelectVO();
        assertThat(lstSelect).isNotNull();
        assertThat(lstSelect.size()).isEqualTo(nomes.length);
    }

    @Test
    @DisplayName("Ao executar delete em TipoLeito retorna sucesso")
    public void delete_TipoLeito_WhenSuccessful() {
        assertThat(repository).isNotNull();

        repoAdapter = new TipoLeitoRepositoryAdapter(repository, new TipoLeitoMapper());
        assertThat(repoAdapter).isNotNull();
        TipoLeito toSave = makeTipoLeitoMockSucesso();

        TipoLeito saved = repoAdapter.save(toSave);
        Optional<TipoLeito> opt = repoAdapter.findById(saved.getId());
        assertTrue(opt.isPresent());

        repoAdapter.delete(saved);
        Optional<TipoLeito> optEmpty = repoAdapter.findById(saved.getId());

        assertTrue(optEmpty.isEmpty());
    }

    private TipoLeito makeTipoLeitoMockSucesso(){
        return TipoLeito.builder()
                .descricao("TESTE")
                .quantidadeCamas(2)
                .build();
    }

    private TipoLeito makeTipoLeitoMockInvalido(){
        return TipoLeito.builder()
                .build();
    }
}