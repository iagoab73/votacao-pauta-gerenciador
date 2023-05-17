package iago.lopes.votacao.pauta.gerenciador.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultadoPautaDTO {
    PautaDTO pauta;

    Long quantidadeVotosSim;

    Long quantidadeVotosNao;
}
