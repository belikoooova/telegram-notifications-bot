package edu.java.bot.configuration.database;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAccessTypeConverter implements Converter<String, AccessType> {
    @Override
    public AccessType convert(String source) {
        return AccessType.fromString(source);
    }
}
