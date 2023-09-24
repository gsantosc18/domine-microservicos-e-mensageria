package com.gedalias.mscartoes.domain.dto;

import com.gedalias.mscartoes.domain.ClienteCartao;

import java.math.BigDecimal;

public record CartoesPorClienteDTO(
    String nome,
    String bandeira,
    BigDecimal limiteLiberado
) {
    public static CartoesPorClienteDTO parse(ClienteCartao clienteCartao) {
        return new CartoesPorClienteDTO(
            clienteCartao.getCartao().getNome(),
            clienteCartao.getCartao().getBandeira().name(),
            clienteCartao.getLimite()
        );
    }
}
