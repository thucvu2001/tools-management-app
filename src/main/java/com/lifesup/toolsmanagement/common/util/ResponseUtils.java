package com.lifesup.toolsmanagement.common.util;

import com.lifesup.toolsmanagement.common.model.ResponseDTO;
import lombok.experimental.UtilityClass;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseUtils {
    public ResponseEntity<ResponseDTO> get(Object result, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .content(result)
                        .hasErrors(false)
                        .errors(null)
                        .timestamp(DateTimeUtils.now())
                        .status(status.value())
                        .build(),
                status
        );
    }

    public ResponseEntity<ResponseDTO> error(RuntimeException runtimeException, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .content(null)
                        .hasErrors(true)
                        .errors(ExceptionUtils.getErrors(runtimeException))
                        .timestamp(DateTimeUtils.now())
                        .status(status.value())
                        .build(),
                status
        );
    }

    public ResponseEntity<ResponseDTO> error(SqlExceptionHelper exception, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .content(null)
                        .hasErrors(true)
                        .errors(ExceptionUtils.gerErrors(exception))
                        .timestamp(DateTimeUtils.now())
                        .status(status.value())
                        .build(),
                status
        );
    }
}
