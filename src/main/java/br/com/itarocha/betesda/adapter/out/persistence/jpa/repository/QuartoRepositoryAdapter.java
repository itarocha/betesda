package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.QuartoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.QuartoMapper;
import br.com.itarocha.betesda.application.out.QuartoRepository;
import br.com.itarocha.betesda.domain.Quarto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuartoRepositoryAdapter implements QuartoRepository {

    private final QuartoJpaRepository repository;
    private final QuartoMapper mapper;

    @Override
    public Quarto save(Quarto model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public Optional<Quarto> findById(Long id) {
        Optional<QuartoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(Quarto model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new RuntimeException("Tipo de Serviço não pode ser excluído. Ação fere as regras de integridade");
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new RuntimeException("Tipo de Serviço não pode ser excluído. Ação fere as regras de integridade");
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Quarto> findAllOrderByQuartoNumero(){
        return repository.findAllOrderByQuartoNumero()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> existeOutroQuartoComEsseNumero(Long quartoId, Integer quartoNumero){
        return repository.existeOutroQuartoComEsseNumero(quartoId, quartoNumero)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> existeOutroQuartoComEsseNumero(Integer numero){
        return repository.existeOutroQuartoComEsseNumero(numero)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> findByDestinacaoHospedagemId(Long quartoId){
        return repository.findByDestinacaoHospedagemId(quartoId)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}
