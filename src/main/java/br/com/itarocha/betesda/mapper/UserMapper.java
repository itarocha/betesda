package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.controller.request.UserRequest;
import br.com.itarocha.betesda.controller.response.UserResponse;
import br.com.itarocha.betesda.persistencia.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserRequest request);

    UserResponse toResponse(UserEntity entity);

    List<UserResponse> toResponseList(List<UserEntity> entities);

    @Mapping(target = "roles", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget UserEntity entity);
}
