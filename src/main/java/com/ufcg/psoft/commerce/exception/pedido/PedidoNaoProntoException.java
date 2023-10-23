package com.ufcg.psoft.commerce.exception.pedido;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class PedidoNaoProntoException extends CommerceException {
    public PedidoNaoProntoException() {
        super("Pedido não está pronto !");
    }
}
