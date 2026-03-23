package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.adapters.in.rest.request.EncaminhadorRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.EncaminhadorResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EncaminhadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface EncaminhadorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    EncaminhadorEntity toEntity(EncaminhadorRequest request);

    @Mapping(target = "entidadeId", source = "entidade.id")
    EncaminhadorResponse toResponse(EncaminhadorEntity entity);

    @Mapping(target = "entidadeId", source = "entidade.id")
    List<EncaminhadorResponse> toResponseList(List<EncaminhadorEntity> entities);

    @Mapping(target = "entidade", ignore = true)
    void updateEntityFromRequest(EncaminhadorRequest request, @MappingTarget EncaminhadorEntity entity);
}
