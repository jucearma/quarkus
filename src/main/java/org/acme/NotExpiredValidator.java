package org.acme;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.acme.NotExpired;

public class NotExpiredValidator implements ConstraintValidator<NotExpired, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        
        if(value == null) return true;

        LocalDate now = LocalDate.now();
        return ChronoUnit.YEARS.between(now,value) > 0;        
    }
}