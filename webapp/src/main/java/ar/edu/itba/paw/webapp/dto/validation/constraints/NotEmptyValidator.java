package ar.edu.itba.paw.webapp.dto.validation.constraints;

import ar.edu.itba.paw.webapp.dto.validation.annotations.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NotEmptyValidator implements ConstraintValidator<Email,String> {

    private static final String PATTERN_STRING = ".+";

    private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return PATTERN.matcher(value).matches();
    }
}
