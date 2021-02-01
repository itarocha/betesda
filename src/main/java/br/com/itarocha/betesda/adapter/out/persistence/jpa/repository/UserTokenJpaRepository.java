package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenJpaRepository extends JpaRepository<UserTokenEntity, Long>{

    List<UserTokenEntity> findByEmail(String email);

    Optional<UserTokenEntity> findByToken(String token);

    @Modifying
    @Query(	"DELETE FROM UserTokenEntity u WHERE u.email = :email")
    void deleteAllWhereEmail(@Param("email") String email);

    List<UserTokenEntity> findByEmailAndToken(String email, String token);

    @Modifying
    @Query("update UserEntity u set u.password = :newPassword where u.email = :email")
    void updatePassword(@Param("email") String email, @Param("newPassword") String newPassword);
}
