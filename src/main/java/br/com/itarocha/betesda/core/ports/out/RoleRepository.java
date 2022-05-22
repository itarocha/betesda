package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.Role;
import br.com.itarocha.betesda.domain.enums.RoleNameEnum;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(RoleNameEnum roleName);

}
