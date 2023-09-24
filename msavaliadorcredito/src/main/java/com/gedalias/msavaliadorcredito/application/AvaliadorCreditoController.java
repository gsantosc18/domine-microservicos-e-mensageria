package com.gedalias.msavaliadorcredito.application;

import com.gedalias.msavaliadorcredito.application.exception.ClientNotFoundException;
import com.gedalias.msavaliadorcredito.application.exception.ErroSolicitacaoCartaoException;
import com.gedalias.msavaliadorcredito.application.exception.MicroserviceCommunicationException;
import com.gedalias.msavaliadorcredito.domain.DadosSolicitacaoEmissaoCartao;
import com.gedalias.msavaliadorcredito.domain.ProtocoloSolicitacaoCartao;
import com.gedalias.msavaliadorcredito.domain.dto.DadosAvaliacaoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("avaliacoes-credito")
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    public AvaliadorCreditoController(AvaliadorCreditoService avaliadorCreditoService) {
        this.avaliadorCreditoService = avaliadorCreditoService;
    }

    @GetMapping
    public String status() {
        return "ok";
    }

    @GetMapping("/situacao-cliente")
    public ResponseEntity<?> consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        try {
            return ResponseEntity.ok(avaliadorCreditoService.obterSituacaoCliente(cpf));
        } catch (ClientNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MicroserviceCommunicationException e) {
            return ResponseEntity.status(requireNonNull(HttpStatus.resolve(e.getStatus()))).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> realizarAvaliacao(@RequestBody DadosAvaliacaoDTO dados) {
        try {
            return ResponseEntity.ok(
                avaliadorCreditoService.realizarAvaliacao(dados)
            );
        } catch (ClientNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MicroserviceCommunicationException e) {
            return ResponseEntity.status(requireNonNull(HttpStatus.resolve(e.getStatus()))).body(e.getMessage());
        }
    }

    @PostMapping("solicitacoes-cartao")
    public ResponseEntity<?> solicitarEmissaoCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
        try {
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao =
                avaliadorCreditoService.solicitacaoEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        } catch (ErroSolicitacaoCartaoException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
