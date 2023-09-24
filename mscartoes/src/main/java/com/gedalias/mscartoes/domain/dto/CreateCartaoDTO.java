package com.gedalias.mscartoes.domain.dto;

import com.gedalias.mscartoes.domain.BandeiraCartao;
import com.gedalias.mscartoes.domain.Cartao;

import java.math.BigDecimal;

public record CreateCartaoDTO(
    String nome,
    BandeiraCartao bandeira,
    BigDecimal renda,
    BigDecimal limite
) {

    public Cartao toDomain() {
        return new Cartao(nome, bandeira, renda, limite);
    }

}
