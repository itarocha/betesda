package br.com.itarocha.betesda.adapter.out.persistence.mapper;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.PessoaEntity;
import br.com.itarocha.betesda.domain.Pessoa;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class PessoaMapper {

    private final EnderecoMapper enderecoMapper;

    public Pessoa toModel(PessoaEntity entity) {
        if (isNull(entity)) return null;

        return Pessoa.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .dataNascimento(entity.getDataNascimento())
                .sexo(entity.getSexo())
                .estadoCivil(entity.getEstadoCivil())
                .cpf(entity.getCpf())
                .cartaoSus(entity.getCartaoSus())
                .rg(entity.getRg())
                .naturalidadeCidade(entity.getNaturalidadeCidade())
                .naturalidadeUf(entity.getNaturalidadeUf())
                .nacionalidade(entity.getNacionalidade())
                .profissao(entity.getProfissao())
                .endereco(enderecoMapper.toModel(entity.getEndereco()))
                .telefone(entity.getTelefone())
                .telefone2(entity.getTelefone2())
                .email(entity.getEmail())
                .observacoes(entity.getObservacoes())
                .build();
    }

    public PessoaEntity toEntity(Pessoa model) {
        if (isNull(model)) return null;

        return PessoaEntity.builder()
                .id(model.getId())
                .nome(model.getNome())
                .dataNascimento(model.getDataNascimento())
                .sexo(model.getSexo())
                .estadoCivil(model.getEstadoCivil())
                .cpf(model.getCpf())
                .cartaoSus(model.getCartaoSus())
                .rg(model.getRg())
                .naturalidadeCidade(model.getNaturalidadeCidade())
                .naturalidadeUf(model.getNaturalidadeUf())
                .nacionalidade(model.getNacionalidade())
                .profissao(model.getProfissao())
                .endereco(enderecoMapper.toEntity(model.getEndereco()))
                .telefone(model.getTelefone())
                .telefone2(model.getTelefone2())
                .email(model.getEmail())
                .observacoes(model.getObservacoes())
                .build();
    }
}