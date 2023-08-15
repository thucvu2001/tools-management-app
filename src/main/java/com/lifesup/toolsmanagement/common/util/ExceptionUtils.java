package com.lifesup.toolsmanagement.common.util;

import lombok.experimental.UtilityClass;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import java.util.List;

@UtilityClass
public class ExceptionUtils {
    public static List<String> getErrors(RuntimeException exception) {
        return exception.getMessage().lines().toList();
    }

    public static List<String> gerErrors(SqlExceptionHelper exception) {
        return exception.toString().lines().toList();
    }
}
