package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.SituacaoLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.SituacaoLeitoMapper;
import br.com.itarocha.betesda.application.out.SituacaoLeitoRepository;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SituacaoLeitoRepositoryAdapter implements SituacaoLeitoRepository {

    private final SituacaoLeitoJpaRepository repository;
    private final SituacaoLeitoMapper mapper;

    @Override
    public SituacaoLeito save(SituacaoLeito model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public Optional<SituacaoLeito> findById(Long id) {
        Optional<SituacaoLeitoEntity> opt = repository.findById(id);
        return Optional.of(opt.isPresent() ? mapper.toModel(opt.get()) : null);
    }

    @Override
    public void delete(SituacaoLeito model) {
        repository.delete(mapper.toEntity(model));
    }

    @Override
    public List<SituacaoLeito> findAllOrderByDescricao() {
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
