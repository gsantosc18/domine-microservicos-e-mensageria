package com.gedalias.mscartoes.application;

import com.gedalias.mscartoes.domain.ClienteCartao;
import com.gedalias.mscartoes.infra.repository.ClienteCartaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteCartaoService {
    private final ClienteCartaoRepository repository;

    public ClienteCartaoService(ClienteCartaoRepository clienteCartaoRepository) {
        this.repository = clienteCartaoRepository;
    }

    public List<ClienteCartao> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
