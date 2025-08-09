package com.fleetmanagement.common.rest;

import com.fleetmanagement.common.exception.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler extends BaseController {
    private static final String DUMMY_ERROR_CODE = "99";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<ErrorResponse> handleException(Exception exception) {
        log.error("Unknown exception occurred", exception);
        return respond(new ErrorResponse(DUMMY_ERROR_CODE, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorResponse> handleInvalidArgumentException(
            MethodArgumentNotValidException methodArgumentNotValidException) {

        log.error("Validation exception occurred", methodArgumentNotValidException);
        String errorMessage =
                methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getField)
                        .map(error -> ExceptionMessage.FIELD_IS_NOT_VALID + "[" + error + "]")
                        .collect(Collectors.joining(" && "));

        return respond(new ErrorResponse(DUMMY_ERROR_CODE, errorMessage));
    }
}
