package dev.coding.springboot.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class TestStubFactory {

    public static void stubForGetSlidesOnSuccess(final WireMockServer wireMockServer, final ObjectMapper objectMapper, final SlideShowData anySlideShowData) throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/json"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(OK.value())
                        .withBody(objectMapper.writeValueAsBytes(anySlideShowData))));
    }

    public static void stubForGetSlidesOnNotFound(final WireMockServer wireMockServer) {
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/json"))
                .willReturn(aResponse()
                        .withStatus(NOT_FOUND.value())));
    }

    public static void stubForGetSlidesOnInternalServerError(final WireMockServer wireMockServer) {
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/json"))
                .willReturn(aResponse().withStatus(INTERNAL_SERVER_ERROR.value())));
    }

    public static void stubForGetSlidesOnNoResponseCode(final WireMockServer wireMockServer, final ObjectMapper objectMapper) throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/json"))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withBody(objectMapper.writeValueAsBytes(null))));
    }
}
