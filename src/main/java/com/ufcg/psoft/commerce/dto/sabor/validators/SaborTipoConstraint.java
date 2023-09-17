package com.ufcg.psoft.commerce.dto.sabor.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = SaborTipoValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SaborTipoConstraint {
  String message() default "Erro de validaçao";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
