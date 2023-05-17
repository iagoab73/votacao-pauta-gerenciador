package iago.lopes.votacao.pauta.gerenciador.dto;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class VotoDTO {
    @NotNull(message = "ID de associado não informado")
    Long idAssociado;

    @Digits(integer = 11, fraction = 0, message = "CPF inválido")
    @NotNull(message = "CPF não informado")
    String cpfAssociado;

    @Pattern(regexp = "[SN]", flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotNull(message = "Voto não informado")
    String voto;
}
