package br.com.itarocha.betesda;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoHospedeEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.TipoHospedeJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    /*
    @Autowired
    private TipoHospedeJpaRepository repository;

    @Test
    void findAll(){
        List<TipoHospedeEntity> lista = repository.findAll();
        assertThat(lista.size()).isNotNull();
        //assertThat().... isEqualTo
    }
    */
}
