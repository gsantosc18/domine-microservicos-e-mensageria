package com.gedalias.msavaliadorcredito.domain;

import java.math.BigDecimal;

public record CartaoAprovado(
    String cartao,
    String bandeira,
    BigDecimal limiteAprovado
) {}
