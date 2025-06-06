package ru.practicum.explorewithme.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.practicum.explorewithme.statclient", "ru.practicum.explorewithme.main"})
public class EwmMainApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmMainApp.class, args);
    }
}
