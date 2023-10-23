package com.ufcg.psoft.commerce.exception.estabelecimento;

import com.ufcg.psoft.commerce.exception.CommerceException;

public class EstabelecimentoCodAcessoException extends CommerceException {
    public EstabelecimentoCodAcessoException() {
        super("Codigo de acesso invalido !");
    }
}
