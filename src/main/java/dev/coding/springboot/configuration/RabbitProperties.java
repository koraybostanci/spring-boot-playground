package dev.coding.springboot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties("rabbitmq")
public class RabbitProperties {

    @NotNull
    private String exchangeName;

    private Entry messageReceived = new Entry();

    private Entry messageDeadLetter = new Entry();

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Entry getMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(Entry messageReceived) {
        this.messageReceived = messageReceived;
    }

    public Entry getMessageDeadLetter() {
        return messageDeadLetter;
    }

    public void setMessageDeadLetter(Entry messageDeadLetter) {
        this.messageDeadLetter = messageDeadLetter;
    }

    @Validated
    public static class Entry {

        @NotNull
        private String queueName;

        @NotNull
        private String routingKey;

        @Min(1)
        private int concurrentConsumerCount;

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

        public int getConcurrentConsumerCount() {
            return concurrentConsumerCount;
        }

        public void setConcurrentConsumerCount(int concurrentConsumerCount) {
            this.concurrentConsumerCount = concurrentConsumerCount;
        }
    }
}
