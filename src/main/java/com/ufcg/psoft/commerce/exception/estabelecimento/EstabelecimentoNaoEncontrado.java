package com.ufcg.psoft.commerce.exception.estabelecimento;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class EstabelecimentoNaoEncontrado extends CommerceException {
  public EstabelecimentoNaoEncontrado() {
    super("O estabelecimento consultado nao existe!");
  }
}
