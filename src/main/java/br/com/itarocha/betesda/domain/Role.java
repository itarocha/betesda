package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.domain.enums.RoleNameEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    private Long id;
    private RoleNameEnum name;
}