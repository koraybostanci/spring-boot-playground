package dev.coding.springboot.demo;

import dev.coding.springboot.demo.quote.Quote;
import dev.coding.springboot.demo.quote.QuoteGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DemoService {

    private final QuoteGateway quoteGateway;

    public DemoService(final QuoteGateway quoteGateway) {
        this.quoteGateway = quoteGateway;
    }

    public Optional<Quote> getQuote() {
        return quoteGateway.getQuote();
    }
}
