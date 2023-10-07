package com.ufcg.psoft.commerce.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "pizza")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    @NotNull(message = "Pizza deve ter pelo menos um sabor")
    private Sabor sabor1;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Sabor sabor2;

    @Column(name = "tamanho")
    @NotNull(message = "Pizza deve ter um tamanho")
    private String tamanho;

    @ManyToMany(mappedBy = "pizzas", fetch = FetchType.LAZY)
    private List<Pedido> pedidos;
    
}
