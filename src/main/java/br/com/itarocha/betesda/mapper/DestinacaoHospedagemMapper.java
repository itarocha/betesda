package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.adapters.in.rest.request.DestinacaoHospedagemRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.DestinacaoHospedagemResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.DestinacaoHospedagemEntity;
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
