package com.integrator.application.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class StringToDateConverter implements Converter<String, Date> {

    private final List<String> formatPatterns = Arrays.asList(
            "yyyy-MM-dd",
            "dd/MM/yyyy",
            "MM-dd-yyyy"
    );

    @Override
    public Date convert(String source) {
        for (String pattern : formatPatterns) {
            try {
                return new SimpleDateFormat(pattern).parse(source);
            } catch (ParseException ignored) {}
        }
        throw new IllegalArgumentException("Invalid date format: " + source);
    }
}
