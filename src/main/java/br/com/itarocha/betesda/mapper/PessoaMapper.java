package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.model.request.PessoaRequest;
import br.com.itarocha.betesda.model.response.PessoaResponse;
import br.com.itarocha.betesda.persistencia.model.PessoaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PessoaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    PessoaEntity toEntity(PessoaRequest request);

    @Mapping(target = "enderecoId", source = "endereco.id")
    PessoaResponse toResponse(PessoaEntity entity);

    @Mapping(target = "enderecoId", source = "endereco.id")
    List<PessoaResponse> toResponseList(List<PessoaEntity> entities);

    @Mapping(target = "endereco", ignore = true)
    void updateEntityFromRequest(PessoaRequest request, @MappingTarget PessoaEntity entity);
}
