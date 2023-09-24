package com.gedalias.msavaliadorcredito.domain.mapper;

import com.gedalias.msavaliadorcredito.domain.Cartao;
import com.gedalias.msavaliadorcredito.domain.CartaoAprovado;

import java.math.BigDecimal;

public class CartaoToCartaoAprovado {

    public static CartaoAprovado map(Cartao cartao, BigDecimal limite) {
        return new CartaoAprovado(
            cartao.nome(),
            cartao.bandeira(),
            limite
        );
    }

}
