package com.gedalias.mscartoes.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cliente_cartao")
public class ClienteCartao {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id private Long id;
    private String cpf;
    @ManyToOne
    @JoinColumn(name = "id_cartao")
    private Cartao cartao;
    private BigDecimal limite;

    public ClienteCartao() {}

    public ClienteCartao(String cpf, Cartao cartao, BigDecimal limite) {
        this.cpf = cpf;
        this.cartao = cartao;
        this.limite = limite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }
}
