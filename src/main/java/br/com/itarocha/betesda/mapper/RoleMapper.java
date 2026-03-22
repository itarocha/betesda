package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.controller.request.RoleRequest;
import br.com.itarocha.betesda.controller.response.RoleResponse;
import br.com.itarocha.betesda.persistencia.model.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    RoleEntity toEntity(RoleRequest request);

    @Mapping(target = "name", source = "name")
    RoleResponse toResponse(RoleEntity entity);

    List<RoleResponse> toResponseList(List<RoleEntity> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(RoleRequest request, @MappingTarget RoleEntity entity);
}
