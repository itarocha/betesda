package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.model.request.QuartoEditRequest;
import br.com.itarocha.betesda.model.request.QuartoRequest;
import br.com.itarocha.betesda.model.response.QuartoResponse;
import br.com.itarocha.betesda.persistencia.model.QuartoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface QuartoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "destinacoes", ignore = true)
    @Mapping(target = "leitos", ignore = true)
    QuartoEntity toEntity(QuartoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "destinacoes", ignore = true)
    @Mapping(target = "leitos", ignore = true)
    QuartoEntity toEntity(QuartoEditRequest request);

    QuartoResponse toResponse(QuartoEntity entity);

    List<QuartoResponse> toResponseList(List<QuartoEntity> entities);

    @Mapping(target = "destinacoes", ignore = true)
    @Mapping(target = "leitos", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(QuartoEditRequest request, @MappingTarget QuartoEntity entity);
}
