package br.com.itarocha.betesda.adapters.in.rest.request;

import br.com.itarocha.betesda.core.domain.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleRequest {

    @NotNull(message = "ID é obrigatório")
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    private RoleName name;
}
