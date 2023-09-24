package com.gedalias.mscartoes.application;

import com.gedalias.mscartoes.domain.Cartao;
import com.gedalias.mscartoes.domain.dto.CreateCartaoDTO;
import com.gedalias.mscartoes.infra.repository.CartaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartaoService {
    private final CartaoRepository repository;

    public CartaoService(CartaoRepository cartaoRepository) {
        this.repository = cartaoRepository;
    }

    public void save(CreateCartaoDTO createCartaoDTO) {
        repository.save(createCartaoDTO.toDomain());
    }

    public List<Cartao> findCartaoRendaMenorIgual(Long valor) {
        final BigDecimal renda = BigDecimal.valueOf(valor);
        return repository.findByRendaLessThanEqual(renda);
    }
}
