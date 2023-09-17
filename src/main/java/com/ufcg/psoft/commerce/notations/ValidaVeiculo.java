package com.ufcg.psoft.commerce.notations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidaVeiculo implements ConstraintValidator<ValidadorVeiculo, String>{

    @Override
    public void initialize(ValidadorVeiculo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String tipoVeiculo, ConstraintValidatorContext context) {
        if (tipoVeiculo != null) {
            var generic = tipoVeiculo.toUpperCase();
            if (generic.equals("MOTO") || generic.equals("CARRO")) {
                return true;
            }
        }
        return false;
    }
}
