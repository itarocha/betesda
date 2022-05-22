package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EncaminhadorEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.domain.Encaminhador;
import br.com.itarocha.betesda.domain.Entidade;
import br.com.itarocha.betesda.domain.ItemDictionary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class EntidadeMapper {

    private final EnderecoMapper enderecoMapper;
    private final EncaminhadorMapper encaminhadorMapper;

    public Entidade toModel(EntidadeEntity entity) {
        if (isNull(entity)) return null;

        List<Encaminhador> encaminhadores = isNull(entity.getEncaminhadores()) ? new ArrayList<>()
                :  entity.getEncaminhadores()
                .stream()
                .map(encaminhadorMapper::toModel)
                .collect(Collectors.toList());

        return Entidade.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cnpj(entity.getCnpj())
                .endereco(enderecoMapper.toModel(entity.getEndereco()))
                .telefone(entity.getTelefone())
                .telefone2(entity.getTelefone2())
                .email(entity.getEmail())
                .observacoes(entity.getObservacoes())
                .encaminhadores(encaminhadores)
                .build();
    }

    public EntidadeEntity toEntity(Entidade model) {
        if (isNull(model)) return null;

        List<EncaminhadorEntity> encaminhadores = isNull(model.getEncaminhadores()) ? new ArrayList<>()
                : model.getEncaminhadores()
                .stream()
                .map(encaminhadorMapper::toEntity)
                .collect(Collectors.toList());

        return EntidadeEntity.builder()
                .id(model.getId())
                .nome(model.getNome())
                .cnpj(model.getCnpj())
                .endereco(enderecoMapper.toEntity(model.getEndereco()))
                .telefone(model.getTelefone())
                .telefone2(model.getTelefone2())
                .email(model.getEmail())
                .observacoes(model.getObservacoes())
                .encaminhadores(encaminhadores)
                .build();
    }

    public ItemDictionary toSelectValueVO(Entidade input){
        return new ItemDictionary(input.getId(), input.getNome());
    }

}