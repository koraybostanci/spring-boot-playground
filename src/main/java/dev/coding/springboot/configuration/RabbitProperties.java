package dev.coding.springboot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties("rabbitmq")
public class RabbitProperties {

    @NotNull
    private String exchangeName;

    private Entry messages = new Entry();

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Entry getMessages() {
        return messages;
    }

    public void setMessages(Entry messages) {
        this.messages = messages;
    }

    @Validated
    public static class Entry {

        @NotNull
        private String queueName;

        @NotNull
        private String routingKey;

        public String getQueueName() {
            return queueName;
        }

        public void setQueueName(String queueName) {
            this.queueName = queueName;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }
    }
}
