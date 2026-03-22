package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.controller.request.HospedagemRequest;
import br.com.itarocha.betesda.controller.response.HospedagemResponse;
import br.com.itarocha.betesda.persistencia.model.HospedagemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface HospedagemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "encaminhador", ignore = true)
    @Mapping(target = "destinacaoHospedagem", ignore = true)
    @Mapping(target = "hospedes", ignore = true)
    @Mapping(target = "servicos", ignore = true)
    HospedagemEntity toEntity(HospedagemRequest request);

    @Mapping(target = "entidadeId", source = "entidade.id")
    @Mapping(target = "encaminhadorId", source = "encaminhador.id")
    @Mapping(target = "destinacaoHospedagemId", source = "destinacaoHospedagem.id")
    HospedagemResponse toResponse(HospedagemEntity entity);

    @Mapping(target = "entidadeId", source = "entidade.id")
    @Mapping(target = "encaminhadorId", source = "encaminhador.id")
    @Mapping(target = "destinacaoHospedagemId", source = "destinacaoHospedagem.id")
    List<HospedagemResponse> toResponseList(List<HospedagemEntity> entities);

    @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "encaminhador", ignore = true)
    @Mapping(target = "destinacaoHospedagem", ignore = true)
    @Mapping(target = "hospedes", ignore = true)
    @Mapping(target = "servicos", ignore = true)
    void updateEntityFromRequest(HospedagemRequest request, @MappingTarget HospedagemEntity entity);
}
