package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    public List<Pedido> findByClienteId(Long clienteId);
    public List<Pedido> findByEstabelecimentoId(Long estabelecimentoId);
    public void deleteByClienteId(Long clienteId);
    public void deleteByEstabelecimentoId(Long estabelecimentoId);
    public List<Pedido> findByClienteIdAndEstabelecimentoId(Long clienteId, Long estabelecimentoId);
    public List<Pedido> findByClienteIdAndEstabelecimentoIdAndStatusEntrega(Long clienteId, Long estabelecimentoId, String statusEntrega);
}
