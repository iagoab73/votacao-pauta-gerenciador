package iago.lopes.votacao.pauta.gerenciador.service;

import iago.lopes.votacao.pauta.gerenciador.dto.NovaPautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.PautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.ResultadoPautaDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.SessaoVotacaoDTO;
import iago.lopes.votacao.pauta.gerenciador.dto.VotoDTO;
import iago.lopes.votacao.pauta.gerenciador.model.SessaoVotacaoPauta;
import iago.lopes.votacao.pauta.gerenciador.repository.PautaRepository;
import iago.lopes.votacao.pauta.gerenciador.repository.SessaoVotacaoPautaRepository;
import iago.lopes.votacao.pauta.gerenciador.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Named
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class PautaService {

    private static final String METODO_VOTAR = "votar";
    private static final String OBJECT_VOTO = "Voto";
    private static final String FIELD_ID_PAUTA = "idPauta";
    private final PautaRepository pautaRepository;

    private final SessaoVotacaoPautaRepository sessaoVotacaoPautaRepository;

    private final VotoRepository votoRepository;

    @Transactional
    public Long cadastrar(NovaPautaDTO novaPautaDTO) {
        return pautaRepository.inserirPauta(novaPautaDTO);
    }

    @Transactional(readOnly = true)
    public PautaDTO obterPorId(Long idPauta) {
        try {
            return pautaRepository.obterPauta(idPauta);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public void abrirSessaoVotacao(Long idPauta, SessaoVotacaoDTO sessaoVotacaoDTO) throws MethodArgumentNotValidException, NoSuchMethodException {
        this.obterPorId(idPauta);
        try {
            sessaoVotacaoPautaRepository.inserirSessaoVotacaoPauta(idPauta, sessaoVotacaoDTO);
        } catch (DuplicateKeyException e) {
            lancarBadRequest(PautaService.class.getMethod("abrirSessaoVotacao", Long.class, SessaoVotacaoDTO.class), 0, "SessaoVotacao", FIELD_ID_PAUTA, "Sessão já foi iniciada para a pauta");
        }
    }

    @Transactional
    public void votar(Long idPauta, VotoDTO votoDTO) throws NoSuchMethodException, MethodArgumentNotValidException {
        this.obterPorId(idPauta);
        try {
            SessaoVotacaoPauta sessaoVotacaoPauta = sessaoVotacaoPautaRepository.obterSessaoVotacaoPauta(idPauta);
            if (sessaoVotacaoPauta.getDataHoraInicio().plusSeconds(sessaoVotacaoPauta.getTempoVotacaoSegundos()).isBefore(LocalDateTime.now())) {
                lancarBadRequest(PautaService.class.getMethod(METODO_VOTAR, Long.class, VotoDTO.class), 0, OBJECT_VOTO, FIELD_ID_PAUTA, "A sessão de votação da pauta já foi encerrada");
            }
            votoRepository.inserirVotoPauta(idPauta, votoDTO);
        } catch (EmptyResultDataAccessException e) {
            lancarBadRequest(PautaService.class.getMethod(METODO_VOTAR, Long.class, VotoDTO.class), 0, OBJECT_VOTO, FIELD_ID_PAUTA, "Não há sessão de votação iniciada para a pauta");
        } catch (DuplicateKeyException e) {
            lancarBadRequest(PautaService.class.getMethod(METODO_VOTAR, Long.class, VotoDTO.class), 1, OBJECT_VOTO, "idAssociado", "O associado informado já realizou seu voto");
        }
    }

    @Transactional(readOnly = true)
    public ResultadoPautaDTO obterResultadoVotacao(Long idPauta) {
        try {
            return votoRepository.obterResultadoVotacao(idPauta);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private static void lancarBadRequest(Method method, int index, String objectName, String fieldName, String message) throws MethodArgumentNotValidException {
        MethodParameter methodParameter = new MethodParameter(method, index);
        FieldError fieldError = new FieldError(objectName, fieldName, message);
        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(objectName, fieldName);
        bindingResult.addError(fieldError);
        throw new MethodArgumentNotValidException(methodParameter, bindingResult);
    }
}
