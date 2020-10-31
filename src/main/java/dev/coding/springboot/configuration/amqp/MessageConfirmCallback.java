package dev.coding.springboot.configuration.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class MessageConfirmCallback implements ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        if (ack) {
            log.info("Acked");
        } else {
            log.warn("NotAcked");
        }
    }
}
