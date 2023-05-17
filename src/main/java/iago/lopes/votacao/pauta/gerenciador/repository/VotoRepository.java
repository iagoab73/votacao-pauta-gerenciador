package iago.lopes.votacao.pauta.gerenciador.repository;

import iago.lopes.votacao.pauta.gerenciador.dto.PautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.ResultadoPautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.VotoDTO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.PreparedStatement;

import static iago.lopes.votacao.pauta.gerenciador.config.DatabaseConfiguration.JDBC_TEMPLATE;

@Named
public class VotoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public VotoRepository(@Named(JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void inserirVotoPauta(Long idPauta, VotoDTO votoDTO) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO VOTO (ID_PAUTA, ID_ASSOCIADO, CPF_ASSOCIADO, VOTO) VALUES(?, ?, ?, ?)");
            ps.setLong(1, idPauta);
            ps.setLong(2, votoDTO.getIdAssociado());
            ps.setString(3, votoDTO.getCpfAssociado());
            ps.setString(4, votoDTO.getVoto().toUpperCase());
            return ps;
        });
    }

    public ResultadoPautaDTO obterResultadoVotacao(Long idPauta) {
        return jdbcTemplate.queryForObject("SELECT" +
                        "  ID, DESCRICAO," +
                        "  (SELECT COUNT(*) FROM voto WHERE id_pauta = ? AND voto = 'S') AS QUANTIDADE_S, " +
                        "  (SELECT COUNT(*) FROM voto WHERE id_pauta = ? AND voto = 'N') AS QUANTIDADE_N " +
                        "FROM " +
                        "  PAUTA " +
                        "WHERE " +
                        "  id = ?",
                (rs, rowNum) ->
                        ResultadoPautaDTO.builder()
                                .pauta(PautaDTO.builder()
                                        .id(rs.getLong("ID"))
                                        .descricao(rs.getString("DESCRICAO"))
                                        .build())
                                .quantidadeVotosSim(rs.getLong("QUANTIDADE_S"))
                                .quantidadeVotosNao(rs.getLong("QUANTIDADE_N"))
                                .build(), idPauta, idPauta, idPauta);
    }
}
