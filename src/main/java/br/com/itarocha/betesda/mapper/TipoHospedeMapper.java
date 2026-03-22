package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.model.request.TipoHospedeRequest;
import br.com.itarocha.betesda.model.response.TipoHospedeResponse;
import br.com.itarocha.betesda.persistencia.model.TipoHospedeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TipoHospedeMapper {

    @Mapping(target = "id", ignore = true)
    TipoHospedeEntity toEntity(TipoHospedeRequest request);

    TipoHospedeResponse toResponse(TipoHospedeEntity entity);

    List<TipoHospedeResponse> toResponseList(List<TipoHospedeEntity> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(TipoHospedeRequest request, @MappingTarget TipoHospedeEntity entity);
}
