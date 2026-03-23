package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.RoleEntity;
import br.com.itarocha.betesda.core.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJPARepository extends JpaRepository<RoleEntity, Long> {

	Optional<RoleEntity> findByName(RoleName roleName);
	
}
