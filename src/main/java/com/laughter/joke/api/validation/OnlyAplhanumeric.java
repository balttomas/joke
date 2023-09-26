package com.laughter.joke.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { OnlyAplhanumeric.Validator.class })
public @interface OnlyAplhanumeric {

  String message() default "Field value may contain only letters and numbers.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<OnlyAplhanumeric, String> {
    private String message;

    @Override
    public void initialize(OnlyAplhanumeric requiredIfChecked) {
      this.message = requiredIfChecked.message();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
      String regex = "^[a-zA-Z0-9]+$";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(value);

      var valid = matcher.matches();

      if (!Boolean.TRUE.equals(valid)) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      }
      return valid;
    }
  }

}
