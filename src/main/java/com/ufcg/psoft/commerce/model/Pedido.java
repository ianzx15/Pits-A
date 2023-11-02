package com.ufcg.psoft.commerce.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "pedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("cliente_id")
    @Column(name = "cliente_id")
    private Long clienteId;

    @JsonProperty("estabelecimento_id")
    @Column(name = "estabelecimento_id")
    private Long estabelecimentoId;

    @JsonProperty("entregador_id")
    @Column(name = "entregador_id")
    @Builder.Default
    private Long entregadorId = 0L;

    @JsonProperty("endereco_entrega")
    @Column(name = "endereco_entrega")
    private String enderecoEntrega;

    @JsonProperty("preco")
    @Column(name = "preco")
    private Double preco;

    @JsonProperty("pizzas")
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "pedido_pizza", joinColumns = {
            @JoinColumn(name = "pedido_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "pizza_id")
    })
    private List<Pizza> pizzas;

    @Column(name = "status_pagamento")
    @JsonProperty("statusPagamento")
    @Builder.Default
    private Boolean statusPagamento = false;

    @Column(name = "status_entrega")
    @JsonProperty("statusEntrega")
    @Builder.Default
    private String statusEntrega = "Pedido recebido";

}
