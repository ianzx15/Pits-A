package com.ufcg.psoft.commerce.exception;

public class EntregadorNotFoundException extends CommerceException {
    public EntregadorNotFoundException() {
        super("O entregador consultado nao existe!");
    }
}
