package dev.coding.springboot.gateway.httpbin.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class SlideShowData implements Serializable {
    private SlideShow slideshow;
}
