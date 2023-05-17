package iago.lopes.votacao.pauta.gerenciador.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SessaoVotacaoPauta {
    Integer tempoVotacaoSegundos;

    LocalDateTime dataHoraInicio;
}
