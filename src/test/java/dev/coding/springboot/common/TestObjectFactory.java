package dev.coding.springboot.common;

import dev.coding.springboot.common.configuration.rabbitmq.RabbitMQProperties.Inbox;
import dev.coding.springboot.httpbin.gateway.data.Slide;
import dev.coding.springboot.httpbin.gateway.data.SlideShow;
import dev.coding.springboot.httpbin.gateway.data.SlideShowData;
import dev.coding.springboot.task.queue.inbox.Task;

import java.util.List;

import static dev.coding.springboot.common.TestConstants.ANY_QUEUE_NAME;
import static dev.coding.springboot.common.TestConstants.ANY_ROUTING_KEY;

public class TestObjectFactory {

    public static Inbox anyInboxQueue() {
        final Inbox inbox = new Inbox();
        inbox.setQueueName(ANY_QUEUE_NAME);
        inbox.setRoutingKey(ANY_ROUTING_KEY);
        return inbox;
    }

    public static Task anyTaskWithName(final String name) {
        final Task task = new Task();
        task.setName(name);
        return task;
    }

    public static SlideShowData anySlideShowData() {
        final SlideShowData slideShowData = new SlideShowData();
        slideShowData.setSlideshow(anySlideShow());
        return slideShowData;
    }

    public static SlideShow anySlideShow() {
        final SlideShow slideShow = new SlideShow();
        slideShow.setAuthor("anyAuthor");
        slideShow.setTitle("anyTitle");
        slideShow.setSlides(List.of(anySlide()));
        return slideShow;
    }

    public static Slide anySlide() {
        final Slide slide = new Slide();
        slide.setTitle("anyTitle");
        slide.setType("anyType");
        slide.setItems(List.of("item1", "item2"));
        return slide;
    }
}
