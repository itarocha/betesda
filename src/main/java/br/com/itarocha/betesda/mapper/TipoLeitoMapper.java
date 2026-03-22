package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.controller.request.TipoLeitoRequest;
import br.com.itarocha.betesda.controller.response.TipoLeitoResponse;
import br.com.itarocha.betesda.persistencia.model.TipoLeitoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TipoLeitoMapper {

    @Mapping(target = "id", ignore = true)
    TipoLeitoEntity toEntity(TipoLeitoRequest request);

    TipoLeitoResponse toResponse(TipoLeitoEntity entity);

    List<TipoLeitoResponse> toResponseList(List<TipoLeitoEntity> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(TipoLeitoRequest request, @MappingTarget TipoLeitoEntity entity);
}
