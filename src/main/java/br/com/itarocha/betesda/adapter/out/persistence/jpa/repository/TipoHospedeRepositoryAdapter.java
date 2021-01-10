package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoHospedeEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoHospedeMapper;
import br.com.itarocha.betesda.application.out.TipoHospedeRepository;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoHospede;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoHospedeRepositoryAdapter implements TipoHospedeRepository {

    private final TipoHospedeJpaRepository repository;
    private final TipoHospedeMapper mapper;

    @Override
    public TipoHospede save(TipoHospede model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public Optional<TipoHospede> findById(Long id) {
        Optional<TipoHospedeEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(TipoHospede model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new RuntimeException("Tipo de Serviço não pode ser excluído. Ação fere as regras de integridade");
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<TipoHospede> findAllOrderByDescricao() {
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
