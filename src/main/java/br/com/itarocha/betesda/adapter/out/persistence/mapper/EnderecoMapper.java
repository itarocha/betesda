package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EnderecoEntity;
import br.com.itarocha.betesda.domain.Endereco;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class EnderecoMapper {

    public Endereco toModel(EnderecoEntity entity) {
        if (isNull(entity)) return null;

        return Endereco.builder()
                .id(entity.getId())
                .logradouro(entity.getLogradouro())
                .numero(entity.getNumero())
                .complemento(entity.getComplemento())
                .bairro(entity.getBairro())
                .cep(entity.getCep())
                .cidade(entity.getCidade())
                .uf(entity.getUf())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }

    public EnderecoEntity toEntity(Endereco model) {
        if (isNull(model)) return null;

        return EnderecoEntity.builder()
                .id(model.getId())
                .logradouro(model.getLogradouro())
                .numero(model.getNumero())
                .complemento(model.getComplemento())
                .bairro(model.getBairro())
                .cep(model.getCep())
                .cidade(model.getCidade())
                .uf(model.getUf())
                .latitude(model.getLatitude())
                .longitude(model.getLongitude())
                .build();
    }

}


