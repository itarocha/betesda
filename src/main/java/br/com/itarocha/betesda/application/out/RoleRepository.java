package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.Role;
import br.com.itarocha.betesda.domain.enums.RoleNameEnum;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(RoleNameEnum roleName);

}
