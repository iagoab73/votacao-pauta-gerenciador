package iago.lopes.votacao.pauta.gerenciador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GerenciadorDeVotacaoDePautasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GerenciadorDeVotacaoDePautasApplication.class, args);
    }

}
