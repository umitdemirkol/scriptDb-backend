package com.oskon.scriptDb.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
@Component
public class GlobalExceptionHandler extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String DEFAULT_ERROR_MESSAGE = "Unable to process this request.";
    private static final Long DEFAULT_ERROR_CODE = 999999L;

    @ExceptionHandler
    public ResponseEntity<?> handle(Exception exception, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        logger.error("Exception: Unable to process this request. ", exception);
        GlobalExceptionMessage globalExceptionMessage;
        if (exception instanceof GlobalException globalException && globalException.getCode() != null) {
            globalExceptionMessage = new GlobalExceptionMessage();
            globalExceptionMessage.setCode(globalException.getCode());
            globalExceptionMessage.setStatus(globalException.getStatus());
            globalExceptionMessage.setMessage(globalException.getMessage());
        } else {
            globalExceptionMessage = new GlobalExceptionMessage();
            globalExceptionMessage.setCode(DEFAULT_ERROR_CODE);
            globalExceptionMessage.setStatus(HttpStatus.BAD_REQUEST);
            globalExceptionMessage.setMessage(DEFAULT_ERROR_MESSAGE);
            globalExceptionMessage.setDebugMessage(exception.getMessage());
        }
        globalExceptionMessage.setPath(((ServletWebRequest) request).getRequest().getServletPath());
        return new ResponseEntity<>(globalExceptionMessage, headers, globalExceptionMessage.getStatus());
    }
}