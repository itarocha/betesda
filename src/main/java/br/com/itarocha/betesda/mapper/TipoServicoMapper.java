package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.model.request.TipoServicoRequest;
import br.com.itarocha.betesda.model.response.TipoServicoResponse;
import br.com.itarocha.betesda.persistencia.model.TipoServicoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TipoServicoMapper {

    @Mapping(target = "id", ignore = true)
    TipoServicoEntity toEntity(TipoServicoRequest request);

    TipoServicoResponse toResponse(TipoServicoEntity entity);

    List<TipoServicoResponse> toResponseList(List<TipoServicoEntity> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(TipoServicoRequest request, @MappingTarget TipoServicoEntity entity);
}
