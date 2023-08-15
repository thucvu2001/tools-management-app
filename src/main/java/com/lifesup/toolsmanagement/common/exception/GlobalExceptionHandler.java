package com.lifesup.toolsmanagement.common.exception;

import com.lifesup.toolsmanagement.common.model.ResponseDTO;
import com.lifesup.toolsmanagement.common.util.ResponseUtils;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleRuntimeException(RuntimeException exception) {
        return ResponseUtils.error(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleSqlExceptionHelper(SqlExceptionHelper exception) {
        return ResponseUtils.error(exception, HttpStatus.BAD_REQUEST);
    }
}
