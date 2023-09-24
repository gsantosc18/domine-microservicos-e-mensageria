package com.gedalias.msclientes.application;

import com.gedalias.msclientes.domain.dto.CreateClientDTO;
import com.gedalias.msclientes.domain.Cliente;
import com.gedalias.msclientes.infra.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void save(CreateClientDTO createClientDTO) {
        clienteRepository.save(createClientDTO.toDomain());
    }

    public Optional<Cliente> getByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
}
