package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.DestinacaoHospedagemMapper;
import br.com.itarocha.betesda.application.out.DestinacaoHospedagemRepository;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinacaoHospedagemRepositoryAdapter implements DestinacaoHospedagemRepository {

    private final DestinacaoHospedagemJpaRepository repository;
    private final DestinacaoHospedagemMapper mapper;

    @Override
    public DestinacaoHospedagem save(DestinacaoHospedagem model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public Optional<DestinacaoHospedagem> findById(Long id) {
        Optional<DestinacaoHospedagemEntity> opt = repository.findById(id);
        return opt.isEmpty() ? Optional.of(mapper.toModel(opt.get())) : Optional.empty();
    }

    @Override
    public void delete(DestinacaoHospedagem model) {
        repository.delete(mapper.toEntity(model));
    }

    @Override
    public List<DestinacaoHospedagem> findAllOrderByDescricao() {
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
