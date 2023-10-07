package com.ufcg.psoft.commerce.exception.pedido;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class MetodoDePagamentoIndisponivel extends CommerceException {
  public MetodoDePagamentoIndisponivel() {
    super("Método de pagamento indisponível");
  }
}
