package org.acme;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER,
    ElementType.TYPE_USE, ElementType.ANNOTATION_TYPE,ElementType.TYPE_PARAMETER,ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {NotExpiredValidator.class})
public @interface NotExpired {

    String message() default "Beer must not be expired";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}