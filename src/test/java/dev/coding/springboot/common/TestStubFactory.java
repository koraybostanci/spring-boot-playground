package dev.coding.springboot.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static dev.coding.springboot.common.TestObjectFactory.configureTestObjectMapper;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class TestStubFactory {

    private static final ObjectMapper objectMapper = configureTestObjectMapper();

    public static void stubForGetSlidesOnSuccess(final SlideShowData anySlideShowData) throws JsonProcessingException {
        stubFor(WireMock.get(urlPathEqualTo("/json"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(OK.value())
                        .withBody(objectMapper.writeValueAsBytes(anySlideShowData))));
    }

}
