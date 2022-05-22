package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.EnderecoJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.EnderecoMapper;
import br.com.itarocha.betesda.core.ports.out.EnderecoRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.Endereco;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoRepositoryImpl implements EnderecoRepository {

    private final EnderecoJpaRepository repository;
    private final EnderecoMapper mapper;

    @Override
    public Endereco save(Endereco model) {
        try {
            return mapper.toModel(repository.save(mapper.toEntity(model)));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Tipo de Serviço"
                    , e.getMostSpecificCause().getMessage());
        }
    }

}