package com.ufcg.psoft.commerce.exception.sabor;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class SaborNaoEncontrado extends CommerceException {
  public SaborNaoEncontrado() {
    super("O sabor consultado nao existe!");
  }
}
