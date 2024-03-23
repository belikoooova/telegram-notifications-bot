package edu.java.bot.configuration.database;

import java.util.Arrays;

public enum AccessType {
    JDBC,
    JPA;

    public static AccessType fromString(String value) {
        for (AccessType type : AccessType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
            "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(AccessType.values()));
    }
}
