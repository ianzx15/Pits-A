package com.ufcg.psoft.commerce.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cliente")
public class Cliente {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("codigoAcesso")
    @Column(name = "codigo_cliente", nullable = false)
    private String codigoAcesso;

    @JsonProperty("nome")
    @Column(name = "nome_cliente", nullable = false)
    private String nome;

    @JsonProperty("endereco")
    @Column(name = "endereco_cliente", nullable = false)
    private String endereco;

}
