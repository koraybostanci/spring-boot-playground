package dev.coding.springboot.demo.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

    private String type;
    private Value value;

    @Data
    public class Value {
        private String id;
        private String quote;
    }
}
