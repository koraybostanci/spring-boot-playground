package dev.coding.springboot.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.common.AbstractIntegrationTest;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static dev.coding.springboot.common.TestObjectFactory.anySlideShowData;
import static dev.coding.springboot.common.TestObjectFactory.configureTestObjectMapper;
import static dev.coding.springboot.common.TestStubFactory.stubForGetSlidesOnSuccess;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@AutoConfigureMockMvc
public class HttpBinControllerIT extends AbstractIntegrationTest {

    private final ObjectMapper objectMapper = configureTestObjectMapper();

    //@Autowired
    //private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void onBeforeEach() {
        wireMockServer.resetAll();
        //mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getSlides_returnsSlideShowData_whenCallToHttpBinEndpointSucceeds() throws Exception {
        final SlideShowData anySlideShowData = anySlideShowData();
        stubForGetSlidesOnSuccess(anySlideShowData);

        final MockHttpServletResponse response = callGetSlidesEndpoint();
        final SlideShowData retrievedSlideShowData = objectMapper.readValue(response.getContentAsByteArray(), SlideShowData.class);

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(retrievedSlideShowData).isEqualTo(anySlideShowData);
    }

    private MockHttpServletResponse callGetSlidesEndpoint() throws Exception {
         return mockMvc.perform(get("/slides"))
                 .andReturn()
                 .getResponse();
    }

}