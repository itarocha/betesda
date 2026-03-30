package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.adapters.in.rest.request.DestinacaoHospedagemRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.DestinacaoHospedagemResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.core.domain.model.DestinacaoHospedagem;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DestinacaoHospedagemMapper {

    @Mapping(target = "id", ignore = true)
    DestinacaoHospedagemEntity toEntity(DestinacaoHospedagemRequest request);

    DestinacaoHospedagemEntity modelToEntity(DestinacaoHospedagem model);

    DestinacaoHospedagemResponse toResponse(DestinacaoHospedagemEntity entity);

    DestinacaoHospedagem entityToModel(DestinacaoHospedagemEntity entity);

    DestinacaoHospedagemResponse toResponse(DestinacaoHospedagem entity);

    List<DestinacaoHospedagemResponse> toResponseList(List<DestinacaoHospedagemEntity> entities);
    List<DestinacaoHospedagem> listEntitytoListModel(List<DestinacaoHospedagemEntity> entities);

    List<DestinacaoHospedagemResponse> listModeltoListaResponse(List<DestinacaoHospedagem> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(DestinacaoHospedagemRequest request, @MappingTarget DestinacaoHospedagemEntity entity);

    @Mapping(target = "id", ignore = true)
    DestinacaoHospedagem requestToModel(DestinacaoHospedagemRequest request);
}
