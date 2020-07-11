package dev.coding.springboot.demo;

import dev.coding.springboot.demo.quote.Quote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class DemoController {

    private static final String PATH_GET_QUOTE = "/quote";

    private final DemoService demoService;

    public DemoController(final DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping(value = PATH_GET_QUOTE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Quote> getQuote() {
        final Optional<Quote> quote = demoService.getQuote();
        if (quote.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return ok(quote.get());
    }

}
