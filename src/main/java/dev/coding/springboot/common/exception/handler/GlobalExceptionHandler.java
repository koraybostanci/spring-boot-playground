package dev.coding.springboot.common.exception.handler;

import dev.coding.springboot.common.exception.BusinessException;
import dev.coding.springboot.common.exception.SystemException;
import dev.coding.springboot.common.exception.business.ConflictException;
import dev.coding.springboot.common.exception.business.NotFoundException;
import dev.coding.springboot.common.exception.business.UnprocessableEntityException;
import dev.coding.springboot.common.exception.problem.Problem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RestControllerAdvice
@Component
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final MediaType mediaTypeProblem = new MediaType("application", "problem+json");
    private static final HttpHeaders headers = new HttpHeaders();
    private static final Map<Class<? extends Exception>, HttpStatus> exceptionsMap = new HashMap<>();

    static {
        headers.setContentType(mediaTypeProblem);

        exceptionsMap.put(SystemException.class, INTERNAL_SERVER_ERROR);
        exceptionsMap.put(BusinessException.class, BAD_REQUEST);
        exceptionsMap.put(ConflictException.class, CONFLICT);
        exceptionsMap.put(NotFoundException.class, NOT_FOUND);
        exceptionsMap.put(UnprocessableEntityException.class, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(final Exception ex) {
        log.warn(getMessage(ex));
        final HttpStatus status = exceptionsMap.getOrDefault(ex.getClass(), INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(problemOf(ex, status), headers, status);
    }

    private Problem problemOf(final Exception ex, final HttpStatus status) {
        return Problem.builder()
                .title(status.getReasonPhrase())
                .status(status.value())
                .message(getMessage(ex))
                .build();
    }
}
