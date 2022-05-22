package br.com.itarocha.betesda.integracao;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.TipoServicoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class TipoServicoEntityIT extends AbstractIntegrationTest{

    @Autowired
    private TipoServicoJpaRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        repository.deleteAll();
    }

}
