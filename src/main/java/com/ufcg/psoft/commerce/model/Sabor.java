package com.ufcg.psoft.commerce.model;

import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "sabores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sabor {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false, name = "nome")
  private String nome;

  @Column(nullable = false, name = "precoM")
  private Double precoM;

  @Column(nullable = false, name = "precoG")
  private Double precoG;

  @Column(nullable = false, name = "tipo")
  private String tipo;

  @Column(nullable = false, name = "disponivel")
  private Boolean disponivel;

  @ManyToOne(fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  private Estabelecimento estabelecimento;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "interesse", joinColumns = {
          @JoinColumn(name = "cliente_id")
  }, inverseJoinColumns = {
          @JoinColumn(name = "sabor_id")
  })
  @PrimaryKeyJoinColumn
  private Set<Cliente> clientesInteressados;
}
