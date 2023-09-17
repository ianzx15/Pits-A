package com.ufcg.psoft.commerce.dto.sabor.validators;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SaborTipoValidator implements ConstraintValidator<SaborTipoConstraint, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    if (StringUtils.isBlank(value)) {
      context.buildConstraintViolationWithTemplate("Tipo obrigatorio").addConstraintViolation();
      return false;
    } else if (!Arrays.asList("salgado", "doce").contains(value)) {
      context.buildConstraintViolationWithTemplate("Tipo deve ser salgado ou doce").addConstraintViolation();
      return false;
    }
    return true;
  }
}
