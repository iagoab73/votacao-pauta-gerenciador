package iago.lopes.votacao.pauta.gerenciador.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NovaPautaDTO {
    @NotBlank(message = "Descrição inválida")
    String descricao;
}
