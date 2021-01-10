package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoServicoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoServicoMapper;
import br.com.itarocha.betesda.application.out.TipoServicoRepository;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoServico;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoServicoRepositoryAdapter implements TipoServicoRepository {

    private final TipoServicoJpaRepository repository;
    private final TipoServicoMapper mapper;

    @Override
    public TipoServico save(TipoServico model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public Optional<TipoServico> findById(Long id) {
        Optional<TipoServicoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(TipoServico model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new RuntimeException("Tipo de Serviço não pode ser excluído. Ação fere as regras de integridade");
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<TipoServico> findAllOrderByDescricao() {
        return repository.findAllOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TipoServico> findAllAtivosOrderByDescricao() {
        return repository.findAllAtivosOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueVO> findAllToSelectVO() {
        return repository.findAllAtivosOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .map(mapper::toSelectValueVO)
                .collect(Collectors.toList());
    }
}