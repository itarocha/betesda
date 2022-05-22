package br.com.itarocha.betesda.containers;

import org.testcontainers.containers.MariaDBContainer;

public class MariaDBTestContainer extends MariaDBContainer<MariaDBTestContainer> {

    public static final String IMAGE_VERSION = "mariadb:10.5.5";
    public static final String DATABASE_NAME = "betesda";
    public static MariaDBContainer container;

    public MariaDBTestContainer() {
        super(IMAGE_VERSION);
    }

    public static MariaDBContainer getInstance(){
        if (container == null){
            container = new MariaDBTestContainer().withDatabaseName(DATABASE_NAME);
        }
        return container;
    }

    @Override
    public void start(){
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());

        for (int i = 0; i < 10; i++) {
            System.out.println("****************************************************");
        }
        System.out.println(container.getJdbcUrl());
        System.out.println(container.getUsername());
        System.out.println(container.getPassword());

        for (int i = 0; i < 10; i++) {
            System.out.println("****************************************************");
        }
    }

    @Override
    public void stop(){

    }
}
