package com.example.orderbot.service;

import com.example.orderbot.model.Stocks;
import com.example.orderbot.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StocksService {

    private final WebClient webClient;

    public Stocks[] getAllStocks(@RequestHeader("Authorization") String token){

        Mono<Stocks[]> monoStocks = this.webClient
                .method(HttpMethod.GET)
                .uri("/stocks")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(Stocks[].class);

        Stocks[] stocks = monoStocks.block();

        return stocks;
    }




}
