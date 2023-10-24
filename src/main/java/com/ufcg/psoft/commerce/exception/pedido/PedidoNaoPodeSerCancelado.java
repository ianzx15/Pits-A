package com.ufcg.psoft.commerce.exception.pedido;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class PedidoNaoPodeSerCancelado extends CommerceException {
    public PedidoNaoPodeSerCancelado() {
        super("Pedido nao pode ser cancelado!");
    }
}
