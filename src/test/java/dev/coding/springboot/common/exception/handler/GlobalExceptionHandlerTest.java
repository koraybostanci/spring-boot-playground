package dev.coding.springboot.common.exception.handler;

import dev.coding.springboot.common.exception.BusinessException;
import dev.coding.springboot.common.exception.SystemException;
import dev.coding.springboot.common.exception.business.ConflictException;
import dev.coding.springboot.common.exception.business.NotFoundException;
import dev.coding.springboot.common.exception.business.UnprocessableEntityException;
import dev.coding.springboot.common.exception.problem.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_VALUE_APPLICATION_PROBLEM_JSON = "application/problem+json";

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void beforeEach() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void handleException_returnsProblemAndHttpStatusCodeAsInternalServerError_onAnyUnspecificRuntimeException() {
        final Exception exception = new RuntimeException("Unspecific Runtime");
        final ResponseEntity responseEntity = exceptionHandler.handleException(exception);
        final Problem problem = (Problem)responseEntity.getBody();

        assertThatResponseEntityIsCorrect(responseEntity, INTERNAL_SERVER_ERROR);
        assertThatProblemIsCorrect(problem, exception, INTERNAL_SERVER_ERROR);
    }

    @Test
    public void handleException_returnsProblemAndHttpStatusCodeAsInternalServerError_onSystemException() {
        final Exception exception = new SystemException("System");
        final ResponseEntity responseEntity = exceptionHandler.handleException(exception);
        final Problem problem = (Problem)responseEntity.getBody();

        assertThatResponseEntityIsCorrect(responseEntity, INTERNAL_SERVER_ERROR);
        assertThatProblemIsCorrect(problem, exception, INTERNAL_SERVER_ERROR);
    }

    @Test
    public void handleException_returnsProblemAndHttpStatusCodeAsBadRequest_onBusinessException() {
        final Exception exception = new BusinessException("Business");
        final ResponseEntity responseEntity = exceptionHandler.handleException(exception);
        final Problem problem = (Problem)responseEntity.getBody();

        assertThatResponseEntityIsCorrect(responseEntity, BAD_REQUEST);
        assertThatProblemIsCorrect(problem, exception, BAD_REQUEST);
    }

    @Test
    public void handleException_returnsProblemAndHttpStatusCodeAsConflict_onConflictException() {
        final Exception exception = new ConflictException("Conflict");
        final ResponseEntity responseEntity = exceptionHandler.handleException(exception);
        final Problem problem = (Problem)responseEntity.getBody();

        assertThatResponseEntityIsCorrect(responseEntity, CONFLICT);
        assertThatProblemIsCorrect(problem, exception, CONFLICT);
    }

    @Test
    public void handleException_returnsProblemAndHttpStatusCodeAsNotFound_onNotFoundException() {
        final Exception exception = new NotFoundException("Not Found");
        final ResponseEntity responseEntity = exceptionHandler.handleException(exception);
        final Problem problem = (Problem)responseEntity.getBody();

        assertThatResponseEntityIsCorrect(responseEntity, NOT_FOUND);
        assertThatProblemIsCorrect(problem, exception, NOT_FOUND);
    }

    @Test
    public void handleException_returnsProblemAndHttpStatusCodeAsUnprocessableEntity_onUnprocessableEntityException() {
        final Exception exception = new UnprocessableEntityException("Unprocessable Entity");
        final ResponseEntity responseEntity = exceptionHandler.handleException(exception);
        final Problem problem = (Problem)responseEntity.getBody();

        assertThatResponseEntityIsCorrect(responseEntity, UNPROCESSABLE_ENTITY);
        assertThatProblemIsCorrect(problem, exception, UNPROCESSABLE_ENTITY);
    }

    private void assertThatResponseEntityIsCorrect(final ResponseEntity responseEntity, final HttpStatus status) {
        assertThat(responseEntity.getHeaders().get(HEADER_KEY_CONTENT_TYPE)).isEqualTo(singletonList(HEADER_VALUE_APPLICATION_PROBLEM_JSON));
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(status.value());
    }

    private void assertThatProblemIsCorrect(final Problem problem, final Exception exception, final HttpStatus status) {
        assertThat(problem.getStatus()).isEqualTo(status.value());
        assertThat(problem.getTitle()).isEqualTo(status.getReasonPhrase());
        assertThat(problem.getMessage()).isEqualTo(getMessage(exception));
        assertThat(problem.getTimestamp()).isGreaterThan(0L);
    }

}