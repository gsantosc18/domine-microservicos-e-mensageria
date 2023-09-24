package com.gedalias.mscartoes.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gedalias.mscartoes.domain.Cartao;
import com.gedalias.mscartoes.domain.ClienteCartao;
import com.gedalias.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import com.gedalias.mscartoes.infra.repository.CartaoRepository;
import com.gedalias.mscartoes.infra.repository.ClienteCartaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmissaoCartoesSubscriber {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;

    public EmissaoCartoesSubscriber(CartaoRepository cartaoRepository,
                                    ClienteCartaoRepository clienteCartaoRepository) {
        this.cartaoRepository = cartaoRepository;
        this.clienteCartaoRepository = clienteCartaoRepository;
    }

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload) {
        try {
            DadosSolicitacaoEmissaoCartao dados =
                new ObjectMapper().readValue(payload, DadosSolicitacaoEmissaoCartao.class);

            Cartao cartao = cartaoRepository.findById(dados.idCartao()).orElseThrow();

            ClienteCartao clienteCartao = new ClienteCartao(dados.cpf(), cartao, dados.limiteLiberado());
            clienteCartaoRepository.save(clienteCartao);
        } catch (JsonProcessingException e) {
            logger.error("Houve uma falha ao deserializar a mensagem : message={}", payload, e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
