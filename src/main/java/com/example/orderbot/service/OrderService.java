package com.example.orderbot.service;

import com.example.orderbot.model.Order;
import com.example.orderbot.model.Stocks;
import com.example.orderbot.model.Users;
import com.example.orderbot.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
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

    private final WalletService walletService;

    String token = "Bearer eyJraWQiOiJOcGpiWG5iNDEzdTVkYzFkcHJ6V2syOF9WT2FoSVdUUVFVb2NXY2hOOTU4IiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULkZuZmlFQi1PZFpMOVQ2TU1JR252ZzRGenEwZXRxMXl5Z2h4cmEtSmQ2QkkiLCJpc3MiOiJodHRwczovL2Rldi04MTUzMjA5Lm9rdGEuY29tL29hdXRoMi9kZWZhdWx0IiwiYXVkIjoiYXBpOi8vZGVmYXVsdCIsImlhdCI6MTY0NzI2MTE2OCwiZXhwIjoxNjQ3MjY0NzY4LCJjaWQiOiIwb2EzbGVsOWQxMnYyZVdVdDVkNyIsInVpZCI6IjAwdTNqdW92OWtLZ0hXcnlXNWQ3Iiwic2NwIjpbInByb2ZpbGUiLCJlbWFpbCIsIm9wZW5pZCJdLCJhdXRoX3RpbWUiOjE2NDcyNjExNjEsInN1YiI6Imp1bGlhbmEubWVsb0Bzb2xpbmZ0ZWMuY29tIn0.ADFXGHFyTOhp45A2JHp-ezx2S_8mb2W5w-GDQzq45NV7RaVAngZJThDl00eYIzMSo4qLK0K4LpE2Aj36jOWvldNzI9Knwy_I2pPKSuTDGZGYSly5DJd6WQanurSt7YtbiebZ1ZH5iCEd0pagKQFpFPMw6iwqwJrj1MUw6Dw9nP8g9_T1-mnntL6sJOuF6FWojEHP8rGVbLqAOKgZPjHL62Mn1gY_30s6xYagibwu9KBfhswZGSiTm-esqGZPYpcoqOJt1wMZ7ECM571SczpirdFWz2DJS_QWAQJTtLpndT-n0I2_mjmb8ves6WidyDibZApH-GRIK5gxDPeHXnmd0A";

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
        if (orders.getType() == 2){
            Wallet wallet = new Wallet();
            wallet.setId_user(orders.getId_user());
            wallet.setId_stock(orders.getId_stock());
            wallet.setStock_name(orders.getStock_name());
            wallet.setStock_symbol(orders.getStock_symbol());
            wallet.setVolume(orders.getVolume());

            walletService.saveStockBalance(wallet, token);
        }
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
