package com.ufcg.psoft.commerce.dto.pedido.validators;

import java.util.List;

import com.ufcg.psoft.commerce.model.Pizza;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PedidoTipoValidador implements ConstraintValidator<PedidoTipoConstraint, List<Pizza>>{

    @Override
    public boolean isValid(List<Pizza> pizzas, ConstraintValidatorContext context) {
        for (Pizza pizza : pizzas) {
            if (!pizza.getTamanho().equals("media") && !pizza.getTamanho().equals("grande")) {
                context.buildConstraintViolationWithTemplate("Tamanho invalido.").addConstraintViolation();
                return false;
            }
            if (pizza.getTamanho().equals("media") && pizza.getSabor2() != null) {
                context.buildConstraintViolationWithTemplate("Pizza media possui apenas um sabor.").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
    
}
