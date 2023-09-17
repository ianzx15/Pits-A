package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.notations.ValidadorVeiculo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Nome e obrigatorio")
    @Column(nullable = false, name = "ent_nome")
    private String nome;

    @JsonProperty("tipoVeiculo")
    @NotBlank(message = "Tipo do veiculo e obrigatorio")
    @ValidadorVeiculo
    @Column(nullable = false, name = "ent_type_veiculo")
    private String tipoVeiculo;

    @JsonProperty("placaVeiculo")
    @NotBlank(message = "Placa do veiculo e obrigatoria")
    @Column(nullable = false, name = "ent_placa_veiculo")
    private String placaVeiculo;

    @JsonProperty("corVeiculo")
    @NotBlank(message = "Cor do veiculo e obrigatoria")
    @Column(nullable = false, name = "ent_color_veiculo")
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @Column(nullable = false, name = "ent_codigoAcesso")
    private String codigoAcesso;
}
