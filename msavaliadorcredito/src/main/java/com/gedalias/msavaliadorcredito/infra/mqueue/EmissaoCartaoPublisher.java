package com.gedalias.msavaliadorcredito.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gedalias.msavaliadorcredito.domain.DadosSolicitacaoEmissaoCartao;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissaoCartoes;

    public EmissaoCartaoPublisher(
        RabbitTemplate rabbitTemplate,
        Queue queueEmissaoCartoes
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueEmissaoCartoes = queueEmissaoCartoes;
    }

    public void solicitacaoCartao(DadosSolicitacaoEmissaoCartao dados)
        throws JsonProcessingException {
        String json = convertIntoJson(dados);
        rabbitTemplate.convertAndSend(queueEmissaoCartoes.getName(), json);
    }

    private String convertIntoJson(DadosSolicitacaoEmissaoCartao dados)
        throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(dados);
    }
}
