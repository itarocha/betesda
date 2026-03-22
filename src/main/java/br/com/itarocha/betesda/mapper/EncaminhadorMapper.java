package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.model.request.EncaminhadorRequest;
import br.com.itarocha.betesda.model.response.EncaminhadorResponse;
import br.com.itarocha.betesda.persistencia.model.EncaminhadorEntity;
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
