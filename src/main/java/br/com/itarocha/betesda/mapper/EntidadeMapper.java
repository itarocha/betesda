package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.adapters.in.rest.request.EntidadeRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.EntidadeResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.core.domain.model.Entidade;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface EntidadeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    @Mapping(target = "encaminhadores", ignore = true)
    EntidadeEntity toEntity(EntidadeRequest request);

    @Mapping(target = "enderecoId", source = "endereco.id")
    EntidadeResponse toResponse(EntidadeEntity entity);

    //@Mapping(target = "enderecoId", source = "endereco.id")
    Entidade toEntidade(EntidadeEntity entity);

    @Mapping(target = "enderecoId", source = "endereco.id")
    List<EntidadeResponse> toResponseList(List<EntidadeEntity> entities);

    @Mapping(target = "endereco", ignore = true)
    @Mapping(target = "encaminhadores", ignore = true)
    void updateEntityFromRequest(EntidadeRequest request, @MappingTarget EntidadeEntity entity);
}
