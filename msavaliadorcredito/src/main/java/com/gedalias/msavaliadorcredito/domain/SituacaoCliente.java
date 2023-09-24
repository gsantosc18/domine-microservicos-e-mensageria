package com.gedalias.msavaliadorcredito.domain;

import java.util.List;

public record SituacaoCliente(
    DadosCliente dadosCliente,
    List<CartoesCliente> cartoesCliente
) {}
