package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.DestinacaoHospedagemRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.LeitoRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl.QuartoRepositoryImpl;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.*;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.Quarto;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

///import static br.com.itarocha.betesda.jooq.model.Tables.QUARTO;
import static java.sql.DriverManager.getConnection;
import static org.assertj.core.api.Assertions.assertThat;

/*
@DataJpaTest(properties = {
        "spring.test.database.replace=NONE",
        "spring.datasource.url=jdbc:tc:postgresql:12:///springboot"
})
*/
//@DataJpaTest
//@TestContainers
//@Sql("/payment.sql")
//@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
//@ContextConfiguration(locations = {"/jooq-spring.xml"})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LeitoRepositoryImplTest {

    private static final String QUARTO_PREFIX = "QUARTO_";
    private static final String DESTINACAO_PREFIX = "DESTINACAO_";


//    new org.jooq.meta.jaxb.Configuration()
//            .withJdbc(new Jdbc()
//.withDriver("com.mysql.cj.jdbc.Driver")
//.withUrl("jdbc:mysql://localhost/testdb")
//.withProperties(
//new Property()
//.withKey("user")
//.withValue("root"),
//new Property()
//.withKey("password")
//.withValue("secret")
//)
//        )


    /*
    @Container
    static PostgreSQLContainer database = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("springboot")
            .withPassword("springboot")
            .withUsername("springboot");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
    }
    */

    @Autowired
    private LeitoJpaRepository leitoRepository;

    @Autowired
    private QuartoJpaRepository quartoRepository;

    @Autowired
    private DestinacaoHospedagemJpaRepository destinacaoRepository;

    @Autowired
    private EntityManager em;
    //@Autowired
    //private Connection con;

    //@Autowired
    //private DSLContext dslContext;

    private QuartoRepositoryImpl quartoRepo;
    //private LeitoRepositoryImpl leitoRepo;
    private DestinacaoHospedagemRepositoryImpl destinacaoRepo;

    @BeforeEach
    void init() throws ClassNotFoundException, SQLException {
        System.out.println("***************** INIT **********************");

        //DSLContext ctx = DSL.using(SQLDialect.H2);
        //ctx.configuration().settings().setRenderNameStyle(RenderNameStyle.AS_IS);


        DestinacaoHospedagemMapper destinacaoHospedagemMapper = new DestinacaoHospedagemMapper();
        QuartoMapper quartoMapper = new QuartoMapper(destinacaoHospedagemMapper);

        TipoLeitoMapper tipoLeitoMapper = new TipoLeitoMapper();
        SituacaoLeitoMapper situacaoLeitoMapper = new SituacaoLeitoMapper();
        LeitoMapper leitoMapper = new LeitoMapper(tipoLeitoMapper, situacaoLeitoMapper, quartoMapper);

        destinacaoRepo = new DestinacaoHospedagemRepositoryImpl(destinacaoRepository, new DestinacaoHospedagemMapper());
        quartoRepo = new QuartoRepositoryImpl(quartoRepository, new QuartoMapper(destinacaoHospedagemMapper));
        //leitoRepo = new LeitoRepositoryImpl(leitoRepository, leitoMapper, dslContext);
    }

    //@Test
    @DisplayName("Teste de injeção")
    void testAdapter() {
        assertThat(leitoRepository).isNotNull();
        assertThat(quartoRepository).isNotNull();
        assertThat(destinacaoRepository).isNotNull();
    }

    ///@Test
    @DisplayName("Ao persistir Quarto retorna sucesso")
    void save_PersistQuarto_WhenSuccessful() throws ClassNotFoundException {


        /*
        try {
            em.
            Connection connection = em.unwrap(Connection.class);
            System.out.println(connection.getSchema());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */

        Set<DestinacaoHospedagem> destinacoes = buildDestinacoesMock(destinacaoRepo,3);

        //List<Quarto> quartos = buildQuartoclesMock(quartoRepo, 3, destinacoes);


        try (Connection con = DriverManager.getConnection(
                "jdbc:h2:~/betesdabd", "sa", "sa")) {

            /*
            System.out.println(
                    DSL.using(con, SQLDialect.H2)
                            .select(QUARTO.ID, QUARTO.NUMERO, QUARTO.DESCRICAO)
                            .from(QUARTO)
                            .fetch()
            );
            */
            String sql = "select schema_name, is_default "+
                    "from information_schema.schemata "+
                    "order by schema_name";

            // Library code here...
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        /*
        Quarto saved = quartoRepo.save(quartos.get(0));

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescricao()).isEqualTo(QUARTO_PREFIX+1);
        assertThat(saved.getDestinacoes()).isNotNull();
        assertThat(saved.getDestinacoes().size()).isEqualTo(3);

        assertThat(quartoRepo.existeOutroQuartoComEsseNumero(1)).isNotNull();
        assertThat(quartoRepo.existeOutroQuartoComEsseNumero(2)).isNotNull();
        assertThat(quartoRepo.existeOutroQuartoComEsseNumero(3)).isNotNull();
        assertThat(quartoRepo.existeOutroQuartoComEsseNumero(4)).isNullOrEmpty();
        */
    }

    private Set<DestinacaoHospedagem> buildDestinacoesMock(DestinacaoHospedagemRepositoryImpl repo, Integer qtd){
        Set<DestinacaoHospedagem> destinacoes = new HashSet<>();
        IntStream.rangeClosed(1, qtd).boxed().forEach(x ->
            destinacoes.add(repo.save(DestinacaoHospedagem.builder().descricao(DESTINACAO_PREFIX+x).build()))
        );
        return destinacoes;
    }

    private List<Quarto> buildQuartosMock(QuartoRepositoryImpl repo, Integer qtd, Set<DestinacaoHospedagem> destinacoes){
        List<Quarto> quartos = new ArrayList<>();
        IntStream.rangeClosed(1, qtd).boxed().forEach(x ->
                quartos.add(repo.save(Quarto.builder()
                        .descricao(QUARTO_PREFIX+x)
                        .numero(x)
                        .ativo(LogicoEnum.S)
                        .destinacoes(destinacoes)
                        .build()))
        );
        return quartos;
    }

}