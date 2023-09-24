package com.gedalias.msavaliadorcredito.domain;

import java.math.BigDecimal;

public record DadosSolicitacaoEmissaoCartao(
    Long idCartao,
    String cpf,
    String endereco,
    BigDecimal limiteLiberado
) {}
