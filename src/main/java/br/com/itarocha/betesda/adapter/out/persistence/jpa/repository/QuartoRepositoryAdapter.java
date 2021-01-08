package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.QuartoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.QuartoMapper;
import br.com.itarocha.betesda.application.out.QuartoRepository;
import br.com.itarocha.betesda.domain.Quarto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuartoRepositoryAdapter implements QuartoRepository {

    private final QuartoJpaRepository repository;
    private final QuartoMapper quartoMapper;

    @Override
    public Quarto save(Quarto model) {
        return quartoMapper.toModel(repository.save(quartoMapper.toEntity(model)));
    }

    @Override
    public Optional<Quarto> findById(Long id) {
        Optional<QuartoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(quartoMapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(Quarto model) {
        repository.delete(quartoMapper.toEntity(model));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Quarto> findAllOrderByQuartoNumero(){
        return repository.findAllOrderByQuartoNumero()
                .stream()
                .map(quartoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> existeOutroQuartoComEsseNumero(Long quartoId, Integer quartoNumero){
        return repository.existeOutroQuartoComEsseNumero(quartoId, quartoNumero)
                .stream()
                .map(quartoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> existeOutroQuartoComEsseNumero(Integer numero){
        return repository.existeOutroQuartoComEsseNumero(numero)
                .stream()
                .map(quartoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> findByDestinacaoHospedagemId(Long quartoId){
        return repository.findByDestinacaoHospedagemId(quartoId)
                .stream()
                .map(quartoMapper::toModel)
                .collect(Collectors.toList());
    }
}
