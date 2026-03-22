package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.model.request.DestinacaoHospedagemRequest;
import br.com.itarocha.betesda.model.response.DestinacaoHospedagemResponse;
import br.com.itarocha.betesda.persistencia.model.DestinacaoHospedagemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DestinacaoHospedagemMapper {

    @Mapping(target = "id", ignore = true)
    DestinacaoHospedagemEntity toEntity(DestinacaoHospedagemRequest request);

    DestinacaoHospedagemResponse toResponse(DestinacaoHospedagemEntity entity);

    List<DestinacaoHospedagemResponse> toResponseList(List<DestinacaoHospedagemEntity> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(DestinacaoHospedagemRequest request, @MappingTarget DestinacaoHospedagemEntity entity);
}
