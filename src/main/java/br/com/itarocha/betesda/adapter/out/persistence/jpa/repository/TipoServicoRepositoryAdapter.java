package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoServicoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoServicoMapper;
import br.com.itarocha.betesda.application.out.TipoServicoRepository;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoServico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return Optional.of(opt.isPresent() ? mapper.toModel(opt.get()) : null);
    }

    @Override
    public void delete(TipoServico model) {
        repository.delete(mapper.toEntity(model));
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
        return repository.findAllOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .map(mapper::toSelectValueVO)
                .collect(Collectors.toList());
    }
}