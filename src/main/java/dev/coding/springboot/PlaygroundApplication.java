package dev.coding.springboot;

import static org.springframework.boot.Banner.Mode.OFF;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlaygroundApplication {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(PlaygroundApplication.class);
        app.setBannerMode(OFF);
        app.run(args);
    }

}
