package iago.lopes.votacao.pauta.gerenciador.controller;

import iago.lopes.votacao.pauta.gerenciador.dto.NovaPautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.PautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.ResultadoPautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.SessaoVotacaoDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.VotoDTO;
import iago.lopes.votacao.pauta.gerenciador.service.PautaService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;

@OpenAPIDefinition(info = @Info(
        title = "Gerenciador de Votacao de Pautas",
        description = "Serviço responsável por gerenciar votações de pautas",
        version = "1.0.0"
))
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class PautaController {

    private final PautaService pautaService;

    @Operation(summary = "Cadastra uma nova pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PostMapping(path = "/pautas", consumes = {"application/json"})
    public ResponseEntity<Long> cadastrar(@Valid @RequestBody NovaPautaDTO novaPautaDTO) {
        Long idPauta = pautaService.cadastrar(novaPautaDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idPauta).toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Obtém uma pauta pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PautaDTO.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
                    content = @Content)})
    @GetMapping(path = "/pautas/{id}", produces = {"application/json"})
    public ResponseEntity<PautaDTO> obter(@PathVariable("id") Long idPauta) {
        return ResponseEntity.ok(pautaService.obterPorId(idPauta));
    }

    @Operation(summary = "Inicia a sessão de votação da pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão de votação aberta"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PutMapping(path = "/pautas/{id}/votacao/abre", consumes = {"application/json"})
    public ResponseEntity<Void> abrirSessaoVotacao(@PathVariable("id") Long idPauta, @Valid @RequestBody SessaoVotacaoDTO sessaoVotacaoDTO) throws MethodArgumentNotValidException, NoSuchMethodException {
        pautaService.abrirSessaoVotacao(idPauta, sessaoVotacaoDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Vota em uma sessão de pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto realizado"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PutMapping(path = "/pautas/{id}/votacao/voto", consumes = {"application/json"})
    public ResponseEntity<Void> votarPauta(@PathVariable("id") Long idPauta, @Valid @RequestBody VotoDTO votoDTO) throws MethodArgumentNotValidException, NoSuchMethodException {
        pautaService.votar(idPauta, votoDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtém o resultado da votação de uma pauta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResultadoPautaDTO.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
                    content = @Content)})
    @GetMapping(path = "/pautas/{id}/votacao/resultado", produces = {"application/json"})
    public ResponseEntity<ResultadoPautaDTO> obterResultadoVotacao(@PathVariable("id") Long idPauta) {
        return ResponseEntity.ok(pautaService.obterResultadoVotacao(idPauta));
    }
}