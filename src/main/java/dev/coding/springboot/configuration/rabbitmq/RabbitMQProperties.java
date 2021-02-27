package dev.coding.springboot.configuration.rabbitmq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@ConfigurationProperties("rabbitmq")
public class RabbitMQProperties {

    @NotBlank
    private String exchangeName;
    @Valid
    private Inbox inbox = new Inbox();
    @Valid
    private DeadLetter deadLetter = new DeadLetter();

    public static class Inbox extends Queue { }
    public static class DeadLetter extends Queue { }

    @Validated
    @Getter
    @Setter
    public static class Queue {
        @NotBlank
        private String queueName;
        @NotBlank
        private String routingKey;
    }
}

