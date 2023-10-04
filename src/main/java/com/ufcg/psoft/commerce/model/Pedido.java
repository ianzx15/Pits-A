package com.ufcg.psoft.commerce.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "estabelecimento_id")
    private Long estabelecimentoId;

    @Column(name = "entregador_id")
    private Long entregadorId;

    @Column(name = "endereco_entrega")
    private String enderecoEntrega;

    @Column(name = "preco")
    private Double preco;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL,
    orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pizza> pizzas;
    
}
