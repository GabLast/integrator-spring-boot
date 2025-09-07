package com.integrator.application.config.converters;

import com.integrator.application.exceptions.InvalidDataFormat;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yy")
    );


    @Override
    public LocalDate convert(String source) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(source, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new InvalidDataFormat("Invalid date format: " + source);

    }
}