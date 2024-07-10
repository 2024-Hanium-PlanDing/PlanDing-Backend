package com.tukorea.planding.global.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {

    /*
    잘못된 URL 예외
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResponse<?> handleNoHandlerFoundException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_URL);
        return CommonUtils.fail(errorResponse);
    }

    /*
    NULL 값
     */
    @ExceptionHandler(NullPointerException.class)
    public CommonResponse<?> handleNoHandlerNullPointException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_URL);
        return CommonUtils.fail(errorResponse);
    }

    /*
    @Valid 핸들러
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /*
    ErrorCode 핸들러
     */
    @ExceptionHandler(value = {BusinessException.class})
    protected CommonResponse<?> handleBusinessException(BusinessException ex) {
        final ErrorCode errorCode = ex.getErrorCode();
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return CommonUtils.fail(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
