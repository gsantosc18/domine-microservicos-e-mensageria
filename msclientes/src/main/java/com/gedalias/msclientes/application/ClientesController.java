package com.gedalias.msclientes.application;

import com.gedalias.msclientes.domain.dto.CreateClientDTO;
import com.gedalias.msclientes.domain.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("clientes")
public class ClientesController {
    private final ClienteService clienteService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ClientesController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateClientDTO createClientDTO) {
        logger.info("Iniciando a criação de cliente");
        try {
            clienteService.save(createClientDTO);
            URI headerLocation = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .query("cpf={cpf}")
                    .buildAndExpand(createClientDTO.cpf())
                    .toUri();
            logger.info("Novo cliente criado com sucesso");
            return ResponseEntity.created(headerLocation).build();
        } catch (Exception ex) {
            logger.info("Houve um erro interno ao criar o cliente: {}", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", ex.getMessage()
                    ));
        }
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<?> findCliente(@RequestParam("cpf") String cpf) {
        logger.info("Iniciando a consulta de clientes");
        Optional<Cliente> cliente = clienteService.getByCpf(cpf);
        if(cliente.isPresent()) {
            logger.info("O cliente consultado foi encontrado com sucesso");
            return ResponseEntity.ok(cliente);
        } else {
            logger.info("Nenhum cliente com o documento [{}] foi encontrado", cpf);
            return ResponseEntity.notFound().build();
        }
    }
}
