package com.example.orderbot.service;

import com.example.orderbot.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WebClient webClient;

        public void saveStockBalance(@RequestBody Wallet wallet, @RequestHeader("Authorization") String token){
                this.webClient
                .method(HttpMethod.POST)
                .uri("/")
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(Mono.just(wallet), Wallet.class)
                .retrieve()
                .bodyToMono(Wallet.class)
                        .block();

    }
}
