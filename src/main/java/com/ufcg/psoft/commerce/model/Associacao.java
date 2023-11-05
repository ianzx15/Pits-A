package com.ufcg.psoft.commerce.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(name = "associacao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Associacao {

    @EmbeddedId
    @Builder.Default
    private AssociacaoKey id = new AssociacaoKey();

    @MapsId("estabelecimentoId")
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Estabelecimento estabelecimento;

    @MapsId("entregadorId")
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Entregador entregador;

    @Column(name = "status")
    @Builder.Default
    private Boolean status = false;

    @Column(name = "disponibilidade")
    @Builder.Default
    private Boolean disponibilidade = false;
}
