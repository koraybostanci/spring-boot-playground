package dev.coding.springboot.httpbin.gateway.data;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SlideShowDataTest {

    @Test
    public void toString_isImplemented() {
        assertThat(new SlideShowData().toString()).startsWith("SlideShowData(");
    }

}