package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoLeitoMapper;
import br.com.itarocha.betesda.application.out.TipoLeitoRepository;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoLeito;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoLeitoRepositoryAdapter implements TipoLeitoRepository {

    private final TipoLeitoJpaRepository repository;
    private final TipoLeitoMapper mapper;

    @Override
    public TipoLeito save(TipoLeito model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public Optional<TipoLeito> findById(Long id) {
        Optional<TipoLeitoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(TipoLeito model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new RuntimeException("Tipo de Leito não pode ser excluído. Ação fere as regras de integridade");
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<TipoLeito> findAllOrderByDescricao() {
        return repository.findAllOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueVO> findAllToSelectVO() {
        return repository.findAllOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .map(mapper::toSelectValueVO)
                .collect(Collectors.toList());
    }
}
