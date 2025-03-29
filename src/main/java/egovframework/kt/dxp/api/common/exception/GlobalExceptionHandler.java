package egovframework.kt.dxp.api.common.exception;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.StringJoiner;

/**
 * @author GEONLEE
 * @since 2024-07-30
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageConfig messageConfig;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        return generateErrorResponse(ErrorCode.NO_DATA, e);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(FileNotFoundException e) {
        return generateErrorResponse(ErrorCode.NO_DATA, e);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(NullPointerException e) {
        return generateErrorResponse(ErrorCode.SERVICE_ERROR, e);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityExist(EntityExistsException e) {
        return generateErrorResponse(ErrorCode.EXISTS_DATA, e);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQL(SQLException e) {
        return generateErrorResponse(ErrorCode.SQL_ERROR, e);
    }

    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        return generateErrorResponse(e.errorCode, e);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentInValid(MethodArgumentNotValidException e) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        e.getFieldErrors().forEach(fieldError -> {
            stringJoiner.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        });
        return generateErrorResponse(ErrorCode.INVALID_PARAMETER, new InvalidParameterException(stringJoiner.toString()));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        return generateErrorResponse(ErrorCode.INVALID_PARAMETER,
                new InvalidParameterException(e.getMessage()));
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(ErrorCode errorCode, Exception e) {
        log.error(errorCode.messageCode(), e);
        return ResponseEntity.ok()
                .body(ErrorResponse.builder()
                        .status(messageConfig.getCode(errorCode))
                        .message(messageConfig.getMessage(errorCode))
                        .detailMessage(e.getMessage())
                        .build());
    }
}
