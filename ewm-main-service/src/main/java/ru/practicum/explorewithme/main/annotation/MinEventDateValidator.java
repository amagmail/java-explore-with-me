package ru.practicum.explorewithme.main.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MinEventDateValidator implements ConstraintValidator<MinEventDate, String> {

    private LocalDateTime dateTimeFrom;

    @Override
    public void initialize(MinEventDate constraintAnnotation) {
        dateTimeFrom = LocalDateTime.now().plusHours(2);
    }

    @Override
    public boolean isValid(String strVal, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime dateVal = LocalDateTime.parse(strVal, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return dateVal == null || dateVal.isEqual(LocalDateTime.from(dateTimeFrom)) || dateVal.isAfter(LocalDateTime.from(dateTimeFrom));
    }
}
