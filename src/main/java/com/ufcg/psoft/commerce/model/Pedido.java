package com.ufcg.psoft.commerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    
    @Column(name = "id")
    private Long id;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "estabelecimento_id")
    private Long estabelecimentoId;

    
    
}
