package com.ufcg.psoft.commerce.dto.cliente;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class ClienteNotFoundException extends CommerceException {
    public ClienteNotFoundException(){
        super("O cliente consultado nao existe!");
    }
}
