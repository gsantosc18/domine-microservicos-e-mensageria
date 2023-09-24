package com.gedalias.msavaliadorcredito.domain;

import java.math.BigDecimal;

public record CartoesCliente(
    String nome,
    String bandeira,
    BigDecimal limiteLiberado
) {}
