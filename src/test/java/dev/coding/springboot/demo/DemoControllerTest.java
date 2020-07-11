package dev.coding.springboot.demo;

import dev.coding.springboot.demo.common.TestObjectFactory;
import dev.coding.springboot.demo.quote.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class DemoControllerTest {

    @Mock
    private DemoService demoService;

    private DemoController demoController;

    @BeforeEach
    public void setUp() {
        demoController = new DemoController(demoService);
    }

    @Test
    public void getQuote_returnsQuote_whenQuoteFound() {
        final Quote expectedQuote = TestObjectFactory.anyQuote();

        when(demoService.getQuote()).thenReturn(Optional.of(expectedQuote));

        final ResponseEntity<Quote> retrievedQuote = demoController.getQuote();

        assertThat(retrievedQuote.getStatusCode()).isEqualTo(OK);
        assertThat(retrievedQuote.getBody()).isEqualTo(expectedQuote);
    }

    @Test
    public void getQuote_doesNotReturnuote_whenQuoteNotFound() {
        when(demoService.getQuote()).thenReturn(empty());

        final ResponseEntity<Quote> retrievedQuote = demoController.getQuote();

        assertThat(retrievedQuote.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(retrievedQuote.getBody()).isNull();
    }

}
