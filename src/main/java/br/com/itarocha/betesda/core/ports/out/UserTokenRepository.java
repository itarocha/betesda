package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.UserToken;

import java.util.List;
import java.util.Optional;

public interface UserTokenRepository {

    UserToken save(UserToken model);

    Optional<UserToken> findById(Long id);

    Optional<UserToken> findByToken(String token);

    List<UserToken> findAllByEmail(String email);

    void delete(UserToken model);

    void deleteAllByEmail(String email);

    List<UserToken> findByEmailAndToken(String email, String token);

    void updatePassword(String email, String newPassword);
}