package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EncaminhadorEntity;
import br.com.itarocha.betesda.domain.Encaminhador;
import br.com.itarocha.betesda.domain.ItemDictionary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class EncaminhadorMapper {

    private final EnderecoMapper enderecoMapper;

    public Encaminhador toModel(EncaminhadorEntity entity) {
        if (isNull(entity)) return null;

        return Encaminhador.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cargo(entity.getCargo())
                .telefone(entity.getTelefone())
                .email(entity.getEmail())
                .ativo(entity.getAtivo())
                .build();
    }

    public EncaminhadorEntity toEntity(Encaminhador model) {
        if (isNull(model)) return null;

        return EncaminhadorEntity.builder()
                .id(model.getId())
                .nome(model.getNome())
                .cargo(model.getCargo())
                .telefone(model.getTelefone())
                .email(model.getEmail())
                .ativo(model.getAtivo())
                .build();
    }

    public ItemDictionary toSelectValueVO(Encaminhador input){
        return new ItemDictionary(input.getId(), input.getNome());
    }

}