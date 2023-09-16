package com.ufcg.psoft.commerce.Util;

import com.ufcg.psoft.commerce.exception.InvalidCodAcessoException;

import java.util.Objects;

public class Util {
    public static void verificaCodAcesso(String codAcesso, String codAcessoEntity) {
        if (!Objects.equals(codAcesso, codAcessoEntity)) {
            throw new InvalidCodAcessoException();
        }
    }
}
