package dev.coding.springboot.demo;

import static dev.coding.springboot.demo.common.TestObjectFactory.buildUri;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.Application;
import dev.coding.springboot.demo.common.TestObjectFactory;
import dev.coding.springboot.demo.quote.Quote;
import dev.coding.springboot.demo.quote.QuoteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Optional;

@ActiveProfiles("integration-test")
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { Application.class })
public class DemoControllerIT {

    private static final String PATH_QUOTE = "/quote";

    @MockBean
    private QuoteGateway quoteGateway;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = webAppContextSetup(context).build();
    }

    @Test
    public void getQuote_returnsOkAndRetrievedQuote_whenQuoteRetrieved() throws Exception {
        final Quote expectedQuote = TestObjectFactory.anyQuote();
        when(quoteGateway.getQuote()).thenReturn(Optional.of(expectedQuote));

        final MockHttpServletResponse response = performHttpGet(buildUri(PATH_QUOTE), OK);

        final Quote retrievedQuote = objectMapper.readValue(response.getContentAsString(), Quote.class);

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(retrievedQuote).isEqualTo(expectedQuote);
    }

    @Test
    public void getQuote_returnsNotFound_whenNoQuoteRetrieved() throws Exception {
        when(quoteGateway.getQuote()).thenReturn(Optional.empty());

        final MockHttpServletResponse response = performHttpGet(buildUri(PATH_QUOTE), NOT_FOUND);

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    private MockHttpServletResponse performHttpGet(final URI uri, final HttpStatus expectedHttpStatus) throws Exception {
        return mockMvc.perform(get(uri).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(expectedHttpStatus.value()))
                .andReturn()
                .getResponse();
    }

}
