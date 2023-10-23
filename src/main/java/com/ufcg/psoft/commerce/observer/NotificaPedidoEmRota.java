package com.ufcg.psoft.commerce.observer;

import com.ufcg.psoft.commerce.model.Entregador;

public interface NotificaPedidoEmRota {
  
  public void notificaEmRota(Long pedidoId, Long clientId, Entregador entregador);

}
