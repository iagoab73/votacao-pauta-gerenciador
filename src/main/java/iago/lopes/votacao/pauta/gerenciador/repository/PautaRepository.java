package iago.lopes.votacao.pauta.gerenciador.repository;

import iago.lopes.votacao.pauta.gerenciador.dto.NovaPautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.PautaDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

import static iago.lopes.votacao.pauta.gerenciador.config.DatabaseConfiguration.JDBC_TEMPLATE;

@Named
public class PautaRepository {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public PautaRepository(@Named(JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long inserirPauta(NovaPautaDTO pauta) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO PAUTA (DESCRICAO) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pauta.getDescricao());
            return ps;
        }, keyHolder);
        return (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    public PautaDTO obterPauta(Long idPauta) {
        return jdbcTemplate.queryForObject("SELECT * FROM PAUTA WHERE ID = ?", (rs, rn) ->
                PautaDTO.builder()
                        .id(rs.getLong("ID"))
                        .descricao(rs.getString("DESCRICAO"))
                        .build(), idPauta);
    }

}
