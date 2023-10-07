package com.ufcg.psoft.commerce.dto.pedido.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PedidoTipoValidador.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PedidoTipoConstraint {
    String message() default "Erro de validaçao";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
