package com.ufcg.psoft.commerce.exception.pedido;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class PedidoNaoPertenceAEntidade extends CommerceException {
    public PedidoNaoPertenceAEntidade(String message) {
        super(message);
    }
}
