package com.ufcg.psoft.commerce.exception.pedido;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class PedidoNaoEncontrado extends CommerceException{
    public PedidoNaoEncontrado() {
        super("O pedido consultado nao existe!");
    }
    
}
