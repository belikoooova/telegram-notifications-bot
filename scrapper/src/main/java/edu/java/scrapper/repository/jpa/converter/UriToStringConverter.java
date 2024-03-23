package edu.java.scrapper.repository.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.URI;

@Converter(autoApply = true)
public class UriToStringConverter implements AttributeConverter<URI, String> {

    @Override
    public String convertToDatabaseColumn(URI attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public URI convertToEntityAttribute(String dbData) {
        return dbData == null ? null : URI.create(dbData);
    }
}
