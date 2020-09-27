package dev.coding.springboot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties("rabbitmq")
@Getter
@Setter
public class RabbitMQProperties {

    @NotNull
    private String exchangeName;
    @Valid
    private Received received = new Received();
    @Valid
    private DeadLetter deadLetter = new DeadLetter();

    public static class Received extends Entry { }

    public static class DeadLetter extends Entry { }

    @Validated
    @Getter
    @Setter
    public static class Entry {
        @NotNull
        private String queueName;
        @NotNull
        private String routingKey;
    }
}
