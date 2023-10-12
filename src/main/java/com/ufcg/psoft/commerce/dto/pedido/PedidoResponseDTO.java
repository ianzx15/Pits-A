package com.ufcg.psoft.commerce.dto.pedido;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Pizza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("cliente_id")
    private Long clienteId;

    @JsonProperty("estabelecimento_id")
    private Long estabelecimentoId;

    @JsonProperty("endereco_entrega")
    private String enderecoEntrega;

    @JsonProperty("preco")
    private Double preco;

    @JsonProperty("pizzas")
    private List<Pizza> pizzas;

    @JsonProperty("statusPagamento")
    private Boolean statusPagamento;

    @JsonProperty("statusEntrega")
    private String statusEntrega;

}
