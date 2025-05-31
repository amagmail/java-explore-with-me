package ru.practicum.explorewithme.main.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinEventDate1hValidator.class)
public @interface MinEventDate1h {
    String message() default "Event date out of min value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
