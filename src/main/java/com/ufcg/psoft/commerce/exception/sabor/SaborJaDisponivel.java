package com.ufcg.psoft.commerce.exception.sabor;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class SaborJaDisponivel extends CommerceException {
  public SaborJaDisponivel() {
    super("O sabor consultado ja esta disponivel!");
  }
}
