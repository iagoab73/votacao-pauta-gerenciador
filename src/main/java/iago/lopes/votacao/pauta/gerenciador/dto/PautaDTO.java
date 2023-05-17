package iago.lopes.votacao.pauta.gerenciador.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PautaDTO {
    Long id;

    String descricao;
}
