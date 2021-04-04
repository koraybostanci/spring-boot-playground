package dev.coding.springboot.httpbin.gateway.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Slide implements Serializable {
    private String title;
    private String type;
    private List<String> items;
}
