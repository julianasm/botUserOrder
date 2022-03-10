package com.example.orderbot.service;


import com.example.orderbot.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UsersService {

    @Autowired
    private WebClient webClient;


    public Users[] getAllUsers(@RequestHeader("Authorization") String token){

        Mono<Users[]> monoUsers = this.webClient
                .method(HttpMethod.GET)
                .uri("/api/users-id")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(Users[].class);

        Users[] users = monoUsers.block();

        return users;
    }
}
