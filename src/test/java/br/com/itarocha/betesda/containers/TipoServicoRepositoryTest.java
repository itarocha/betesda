package br.com.itarocha.betesda.containers;

import br.com.itarocha.betesda.BetesdaApplication;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoServicoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.TipoServicoJpaRepository;
import br.com.itarocha.betesda.config.ContainersEnvironment;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = BetesdaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TipoServicoRepositoryTest extends ContainersEnvironment {

    @Autowired
    private TipoServicoJpaRepository tsRepo;

    /*
    @Test
    void whenGetListExpectEmptyList(){
        List<TipoServicoEntity> list = tsRepo.findAll();
        assertEquals(0, list.size());
        TipoServicoEntity ts = new TipoServicoEntity();
        ts.setDescricao("TESTE");
        ts.setAtivo(LogicoEnum.S);

        tsRepo.save(ts);

        list = tsRepo.findAll();
        assertEquals(1, list.size());
    }
     */
}
