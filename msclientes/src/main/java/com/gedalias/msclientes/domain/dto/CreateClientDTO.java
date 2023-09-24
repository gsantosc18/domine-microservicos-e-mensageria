package com.gedalias.msclientes.domain.dto;

import com.gedalias.msclientes.domain.Cliente;

public record CreateClientDTO(
        String cpf,
        String nome,
        Integer idade
){
    public Cliente toDomain() {
        return new Cliente(cpf, nome, idade);
    }
}
