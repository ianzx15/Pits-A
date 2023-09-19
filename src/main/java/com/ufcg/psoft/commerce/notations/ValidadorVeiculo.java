package com.ufcg.psoft.commerce.notations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidaVeiculo.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Tipo do veiculo e obrigatorio")

public @interface ValidadorVeiculo {

    String message() default "Tipo do veiculo deve ser moto ou carro";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
