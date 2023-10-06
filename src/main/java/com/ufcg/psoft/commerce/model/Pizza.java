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
    private Sabor sabor1;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Sabor sabor2;

    @Column(name = "tamanho")
    private String tamanho;

    @ManyToMany(mappedBy = "pizzas", fetch = FetchType.LAZY)
    private List<Pedido> pedidos;

    public Double calculaSubTotal() {
        Double valor = 0.0;
        if (tamanho.equals("media")) {
            valor += sabor1.getPrecoM();
        } else if (tamanho.equals("grande")) {
            valor += sabor1.getPrecoG();
            if (sabor2 != null) {
                valor += sabor2.getPrecoG();
                valor /= 2;
            }
        }
        return valor;
    }
    
}
