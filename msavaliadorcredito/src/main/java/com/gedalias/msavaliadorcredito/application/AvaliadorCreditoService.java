package com.gedalias.msavaliadorcredito.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gedalias.msavaliadorcredito.application.exception.ClientNotFoundException;
import com.gedalias.msavaliadorcredito.application.exception.ErroSolicitacaoCartaoException;
import com.gedalias.msavaliadorcredito.application.exception.MicroserviceCommunicationException;
import com.gedalias.msavaliadorcredito.domain.*;
import com.gedalias.msavaliadorcredito.domain.dto.DadosAvaliacaoDTO;
import com.gedalias.msavaliadorcredito.domain.mapper.CartaoToCartaoAprovado;
import com.gedalias.msavaliadorcredito.infra.clients.CartoesResourceClient;
import com.gedalias.msavaliadorcredito.infra.clients.ClientesResourceClient;
import com.gedalias.msavaliadorcredito.infra.mqueue.EmissaoCartaoPublisher;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvaliadorCreditoService {

    private final ClientesResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;
    private final EmissaoCartaoPublisher emissaoCartaoPublisher;

    public AvaliadorCreditoService(ClientesResourceClient clientResourceClient,
                                   CartoesResourceClient cartoesClient,
                                   EmissaoCartaoPublisher emissaoCartaoPublisher) {
        this.clientesClient = clientResourceClient;
        this.cartoesClient = cartoesClient;
        this.emissaoCartaoPublisher = emissaoCartaoPublisher;
    }

    public SituacaoCliente obterSituacaoCliente(String cpf)
        throws ClientNotFoundException, MicroserviceCommunicationException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartoesCliente>> dadosCartoesResponse = cartoesClient.getCartoesByCliente(cpf);
            return new SituacaoCliente(dadosClienteResponse.getBody(), dadosCartoesResponse.getBody());
        } catch (FeignException.FeignClientException ex) {
            final int status = ex.status();
            if(status == HttpStatus.NOT_FOUND.value()) {
                throw new ClientNotFoundException();
            }

            throw new MicroserviceCommunicationException(ex.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(DadosAvaliacaoDTO dadosAvaliacaoDTO)
        throws ClientNotFoundException, MicroserviceCommunicationException {
        try {
            ResponseEntity<DadosCliente> dadosCliente =
                clientesClient.dadosCliente(dadosAvaliacaoDTO.cpf());
            ResponseEntity<List<Cartao>> cartoesRendaAte =
                cartoesClient.getCartoesRendaAte(dadosAvaliacaoDTO.renda());

            DadosCliente cliente = dadosCliente.getBody();
            List<Cartao> cartoes = cartoesRendaAte.getBody();

            List<CartaoAprovado> cartoesAprovados = cartoes
                .stream()
                .map(cartao ->
                    CartaoToCartaoAprovado.map(
                        cartao,
                        calculaLimiteAprovado(
                            cliente.idade(),
                            cartao.limiteBasico()
                        )
                    )
                ).collect(Collectors.toList());
            return new RetornoAvaliacaoCliente(cartoesAprovados);
        } catch (FeignException.FeignClientException ex) {
            int status = ex.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new ClientNotFoundException();
            }
            throw new MicroserviceCommunicationException(ex.getMessage(),status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitacaoEmissaoCartao(DadosSolicitacaoEmissaoCartao dados)
        throws ErroSolicitacaoCartaoException {
        try {
            emissaoCartaoPublisher.solicitacaoCartao(dados);
            String protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (JsonProcessingException e) {
            throw new ErroSolicitacaoCartaoException("Houve uma falha na conversão da mensagem");
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }

    private BigDecimal calculaLimiteAprovado(Long idadeCliente, BigDecimal limiteBaseCartao) {
        BigDecimal idade = BigDecimal.valueOf(idadeCliente);
        BigDecimal fator = idade.divide(BigDecimal.TEN);
        return fator.multiply(limiteBaseCartao);
    }
}
