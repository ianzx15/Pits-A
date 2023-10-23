package com.ufcg.psoft.commerce.exception.pedido;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class SemEntregadoresDispException extends CommerceException {
    public SemEntregadoresDispException() {
        super("Sem entregadores disponiveis no momento !");
    }
}
