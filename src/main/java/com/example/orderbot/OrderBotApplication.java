package com.example.orderbot;

import com.example.orderbot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class OrderBotApplication {

    private final OrderService orderService;

    public static void main(String[] args) {
        SpringApplication.run(OrderBotApplication.class, args);

    }


}
