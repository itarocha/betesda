package br.com.itarocha.betesda.config;

import br.com.itarocha.betesda.containers.MariaDBTestContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// @Testcontainers
public class ContainersEnvironment {

    @Container
    public static MariaDBContainer mariaDBContainer = MariaDBTestContainer.getInstance();

    /*
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
    }
        */
}
