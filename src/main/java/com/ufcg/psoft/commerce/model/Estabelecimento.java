package com.ufcg.psoft.commerce.model;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "estabelecimento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "codigoAcesso")
    private String codigoAcesso;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Sabor> sabores;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Associacao> associacoes;

    @JsonProperty("entregadores")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JoinColumn(name = "entregadores")
    private List<Entregador> entregadoresDisponiveis = new LinkedList<>();
}
