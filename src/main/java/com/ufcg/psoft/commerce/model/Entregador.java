package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "entregadores")
public class Entregador {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_id_entregador")
    private long id;

    @JsonProperty("nome")
    @Column(nullable = false, name = "ent_nome")
    private String nome;

    @JsonProperty("tipoVeiculo")
    @Column(nullable = false, name = "ent_type_veiculo")
    private String tipoVeiculo;

    @JsonProperty("placaVeiculo")
    @Column(nullable = false, name = "ent_placa_veiculo")
    private String placaVeiculo;

    @JsonProperty("corVeiculo")
    @Column(nullable = false, name = "ent_color_veiculo")
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @Column(nullable = false, name = "ent_codigoAcesso")
    private String codigoAcesso;
}
