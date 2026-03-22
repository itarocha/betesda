package br.com.itarocha.betesda.mapper;

import br.com.itarocha.betesda.controller.request.LeitoEditRequest;
import br.com.itarocha.betesda.controller.request.LeitoRequest;
import br.com.itarocha.betesda.controller.response.LeitoResponse;
import br.com.itarocha.betesda.persistencia.model.LeitoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface LeitoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quarto", ignore = true)
    @Mapping(target = "tipoLeito", ignore = true)
    @Mapping(target = "situacao", ignore = true)
    LeitoEntity toEntity(LeitoRequest request);

    @Mapping(target = "quarto", ignore = true)
    @Mapping(target = "tipoLeito", ignore = true)
    @Mapping(target = "situacao", ignore = true)
    LeitoEntity toEntity(LeitoEditRequest request);

    LeitoResponse toResponse(LeitoEntity entity);

    List<LeitoResponse> toResponseList(List<LeitoEntity> entities);

    @Mapping(target = "quarto", ignore = true)
    @Mapping(target = "tipoLeito", ignore = true)
    @Mapping(target = "situacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(LeitoEditRequest request, @MappingTarget LeitoEntity entity);
}
