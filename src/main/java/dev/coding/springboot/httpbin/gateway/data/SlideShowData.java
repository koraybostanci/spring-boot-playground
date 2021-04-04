package dev.coding.springboot.httpbin.gateway.data;

import dev.coding.springboot.httpbin.gateway.data.SlideShow;
import lombok.Data;

import java.io.Serializable;

@Data
public class SlideShowData implements Serializable {
    private SlideShow slideshow;
}
