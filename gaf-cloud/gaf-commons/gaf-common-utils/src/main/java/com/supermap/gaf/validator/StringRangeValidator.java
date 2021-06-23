package com.supermap.gaf.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 校验字符串是否在指定的集合中
 * @author wxl
 * @since 2021/6/18
 */
public class StringRangeValidator implements ConstraintValidator<StringRange, String> {

    private Set<String> range;

    @Override
    public void initialize(StringRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        Field[] declaredFields = constraintAnnotation.entityClass().getDeclaredFields();
        Set<String> fieldNames = Arrays.stream(declaredFields).map(field -> field.getName().replaceAll("([A-Z])", "_$1").toLowerCase()).collect(Collectors.toSet());
        Set<String> specifiedRange = Arrays.stream(constraintAnnotation.value()).map(String::toLowerCase).collect(Collectors.toSet());
        fieldNames.addAll(specifiedRange);
        range= fieldNames;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value) return true;
        return range.contains(value.toLowerCase());
    }

}
