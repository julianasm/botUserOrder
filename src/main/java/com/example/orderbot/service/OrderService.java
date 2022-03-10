package com.example.orderbot.service;

import com.example.orderbot.model.Order;
import com.example.orderbot.model.Stocks;
import com.example.orderbot.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final UsersService usersService;

    private final WebClient webClient;

    String token = "Bearer eyJraWQiOiJOcGpiWG5iNDEzdTVkYzFkcHJ6V2syOF9WT2FoSVdUUVFVb2NXY2hOOTU4IiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULnZhVEZSbjI3bXVPeUptTFdmRUU3UTdTS1p1QlJVRWJKS2tZSl9uTUlzc1kiLCJpc3MiOiJodHRwczovL2Rldi04MTUzMjA5Lm9rdGEuY29tL29hdXRoMi9kZWZhdWx0IiwiYXVkIjoiYXBpOi8vZGVmYXVsdCIsImlhdCI6MTY0NjkzNDc2MiwiZXhwIjoxNjQ2OTM4MzYyLCJjaWQiOiIwb2EzbGVsOWQxMnYyZVdVdDVkNyIsInVpZCI6IjAwdTNqdW92OWtLZ0hXcnlXNWQ3Iiwic2NwIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCJdLCJhdXRoX3RpbWUiOjE2NDY5MzExODIsInN1YiI6Imp1bGlhbmEubWVsb0Bzb2xpbmZ0ZWMuY29tIn0.GO-zJfQZ3OvI9MD8qbwaovIWVtCY_9I556pyhvff0aKbgZ-uHVDo-DjzvneFAQTKxEn9cq2Z-MF3xCUo4kbxlFhgjfO2r8zX1bKJoaqcbazXm7wD4YJEiMz4X8j9ZxEkUlVeT5VM7wWFBmo4iyeDvkMjsd5JNX5rktr0WQNkYt9dT81Xeygs8C2X5W_gfVT_Ki5tpsNb0G8TL_swhB65ZmreVdEy_rtcKqEC1fk8D8fXKdas4VUhiKbPfEwGYjqqbY-CglJ2Brck1Q5GNLm367k5uEFhSjZuCmULonuqMHRc7eGq0vlTGX7zG0nk45d1VlKOs-ch6rNqq9L8ZAZn6g";

    Users users1;
    Stocks stocks1;

    public ResponseEntity<Users[]> getAllUsers(String token){

        Users[] users = this.usersService.getAllUsers(token);
        getRandomUser(users);
        return ResponseEntity.ok(users);

    }

    private final StocksService stocksService;

    public ResponseEntity<Stocks[]> getAllStocks(String token){
        Stocks[] stocks = this.stocksService.getAllStocks(token);
        getRandomStock(stocks);
        return ResponseEntity.ok(stocks);
    }


    public void getRandomUser(Users[] users){
        List<Users> usersList = Arrays.asList(users);
        Random rand = new Random();
        this.users1 = usersList.get(rand.nextInt(usersList.size()));
    }

    public void getRandomStock(Stocks[] stocks){
        List<Stocks> stocksList = Arrays.asList(stocks);
        Random rand = new Random();
        this.stocks1 = stocksList.get(rand.nextInt(stocksList.size()));
    }

    public void generateNewOrder(){
        Random generateValue = new Random();


        Order orders = new Order();
        orders.setId_user(this.users1.getId());
        orders.setId_stock(this.stocks1.getId());
        orders.setStock_name(this.stocks1.getStock_name());
        orders.setStock_symbol(this.stocks1.getStock_symbol());
        orders.setStatus(1);
        orders.setType(generateValue.nextInt(1, 3));
        var volume = generateValue.nextLong(1, 20);
        orders.setVolume(volume);
        orders.setRemaining_volume(volume);
        orders.setPrice(generateValue.nextDouble(1, 300));

        this.saveOrder(orders, token);

    }

    public void orderLoop(){
        int i = 0;
        while (i < 20){
            getAllUsers(token);
            getAllStocks(token);
            generateNewOrder();
        }
    }

    @PostConstruct
    public void callLoop(){

        this.orderLoop();
    }


    public void saveOrder(@RequestBody Order order, @RequestHeader("Authorization") String token){

        Order monoOrder = this.webClient
                .method(HttpMethod.POST)
                .uri("/api/new_order")
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(Mono.just(order), Order.class)
                .retrieve()
                .bodyToMono(Order.class).block();
    }

}
