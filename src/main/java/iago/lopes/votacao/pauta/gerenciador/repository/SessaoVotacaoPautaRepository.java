package iago.lopes.votacao.pauta.gerenciador.repository;

import iago.lopes.votacao.pauta.gerenciador.dto.SessaoVotacaoDTO;
import iago.lopes.votacao.pauta.gerenciador.model.SessaoVotacaoPauta;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.PreparedStatement;
import java.util.Optional;

import static iago.lopes.votacao.pauta.gerenciador.Constants.TEMPO_VOTACAO_DEFAULT;
import static iago.lopes.votacao.pauta.gerenciador.config.DatabaseConfiguration.JDBC_TEMPLATE;

@Named
public class SessaoVotacaoPautaRepository {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public SessaoVotacaoPautaRepository(@Named(JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void inserirSessaoVotacaoPauta(Long idPauta, SessaoVotacaoDTO sessaoVotacaoDTO) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO SESSAO_VOTACAO_PAUTA (ID_PAUTA, TEMPO_VOTACAO) VALUES(?, ?)");
            ps.setLong(1, idPauta);
            ps.setInt(2, Optional.ofNullable(sessaoVotacaoDTO.getTempoVotacaoSegundos()).orElse(TEMPO_VOTACAO_DEFAULT));
            return ps;
        });
    }

    public SessaoVotacaoPauta obterSessaoVotacaoPauta(Long idPauta) {
        return jdbcTemplate.queryForObject("SELECT * FROM SESSAO_VOTACAO_PAUTA WHERE ID_PAUTA = ?", (rs, rn) ->
                SessaoVotacaoPauta.builder()
                        .tempoVotacaoSegundos(rs.getInt("tempo_votacao"))
                        .dataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime())
                        .build(), idPauta);
    }
}
