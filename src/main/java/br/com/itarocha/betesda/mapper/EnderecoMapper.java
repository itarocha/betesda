package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.adapters.in.rest.request.EnderecoRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.EnderecoResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EnderecoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface EnderecoMapper {

    @Mapping(target = "id", ignore = true)
    EnderecoEntity toEntity(EnderecoRequest request);

    @Mapping(target = "uf", source = "uf")
    EnderecoResponse toResponse(EnderecoEntity entity);

    @Mapping(target = "uf", source = "uf")
    List<EnderecoResponse> toResponseList(List<EnderecoEntity> entities);

    void updateEntityFromRequest(EnderecoRequest request, @MappingTarget EnderecoEntity entity);
}
