package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    public List<Pedido> findByClienteId(Long clienteId);
    public List<Pedido> findByEstabelecimentoId(Long estabelecimentoId);
}
