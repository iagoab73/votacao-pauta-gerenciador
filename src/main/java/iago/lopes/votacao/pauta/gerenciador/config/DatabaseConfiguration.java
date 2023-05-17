package iago.lopes.votacao.pauta.gerenciador.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Named;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    public static final String DATASOURCE = "DS";

    public static final String JDBC_TEMPLATE = "JDBC";

    @Value("${database_user}")
    private String user;
    @Value("${database_password}")
    private String pass;

    @Bean
    @Named(DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSourceDB() {
        return DataSourceBuilder
                .create()
                .username(user)
                .password(pass)
                .build();
    }

    @Bean
    @Named(JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate(@Named(DATASOURCE) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
