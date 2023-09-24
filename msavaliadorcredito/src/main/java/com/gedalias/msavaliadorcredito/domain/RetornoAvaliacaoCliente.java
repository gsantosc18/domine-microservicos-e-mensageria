package com.gedalias.msavaliadorcredito.domain;

import java.util.List;

public record RetornoAvaliacaoCliente(
    List<CartaoAprovado> cartoes
) {}
