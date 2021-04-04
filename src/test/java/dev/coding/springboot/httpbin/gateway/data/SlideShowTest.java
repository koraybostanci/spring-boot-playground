package dev.coding.springboot.httpbin.gateway.data;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SlideShowTest {

    @Test
    public void toString_isImplemented() {
        assertThat(new SlideShow().toString()).startsWith("SlideShow(");
    }

}