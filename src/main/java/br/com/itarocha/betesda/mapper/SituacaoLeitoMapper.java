package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.adapters.in.rest.request.SituacaoLeitoRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.SituacaoLeitoResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.SituacaoLeitoEntity;
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
