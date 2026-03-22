package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.model.request.EntidadeRequest;
import br.com.itarocha.betesda.model.response.EntidadeResponse;
import br.com.itarocha.betesda.persistencia.model.EntidadeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface EntidadeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    @Mapping(target = "encaminhadores", ignore = true)
    EntidadeEntity toEntity(EntidadeRequest request);

    @Mapping(target = "enderecoId", source = "endereco.id")
    EntidadeResponse toResponse(EntidadeEntity entity);

    @Mapping(target = "enderecoId", source = "endereco.id")
    List<EntidadeResponse> toResponseList(List<EntidadeEntity> entities);

    @Mapping(target = "endereco", ignore = true)
    @Mapping(target = "encaminhadores", ignore = true)
    void updateEntityFromRequest(EntidadeRequest request, @MappingTarget EntidadeEntity entity);
}
