package ru.practicum.explorewithme.main.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MinEventDate2hValidator implements ConstraintValidator<MinEventDate2h, String> {

    private LocalDateTime dateTimeFrom;

    @Override
    public void initialize(MinEventDate2h constraintAnnotation) {
        dateTimeFrom = LocalDateTime.now().plusHours(2);
    }

    @Override
    public boolean isValid(String strVal, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime dateVal = LocalDateTime.parse(strVal, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return dateVal == null || dateVal.isEqual(LocalDateTime.from(dateTimeFrom)) || dateVal.isAfter(LocalDateTime.from(dateTimeFrom));
    }
}
