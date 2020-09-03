package dev.coding.springboot.rest.controller;

import dev.coding.springboot.event.EventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final EventPublisher eventPublisher;

    public TestController(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/test")
    public String test() {
        return "Ok";
    }

    @GetMapping("/send/{data}")
    public void sendMessage(@PathVariable("data") String data) {
        eventPublisher.publish(data);
    }

}
