package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.RoleEntity;
import br.com.itarocha.betesda.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJPARepository extends JpaRepository<RoleEntity, Long> {

	Optional<RoleEntity> findByName(RoleName roleName);
	
}
