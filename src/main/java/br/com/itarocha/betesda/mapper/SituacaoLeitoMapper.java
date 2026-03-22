package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.controller.request.SituacaoLeitoRequest;
import br.com.itarocha.betesda.controller.response.SituacaoLeitoResponse;
import br.com.itarocha.betesda.persistencia.model.SituacaoLeitoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SituacaoLeitoMapper {

    @Mapping(target = "id", ignore = true)
    SituacaoLeitoEntity toEntity(SituacaoLeitoRequest request);

    SituacaoLeitoResponse toResponse(SituacaoLeitoEntity entity);

    List<SituacaoLeitoResponse> toResponseList(List<SituacaoLeitoEntity> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(SituacaoLeitoRequest request, @MappingTarget SituacaoLeitoEntity entity);
}
