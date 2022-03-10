package com.example.orderbot.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Order {

    private Long id_user;
    private Long id_stock;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private Integer type;
    private Integer status;
    private Double price;
    private Long remaining_volume;


    public Order(Users users, Stocks stocks, String stock_symbol, String stock_name, Long volume, Integer type, Integer status, Double price, Long remaining_volume){
        this.id_user = users.getId();
        this.id_stock = stocks.getId();
        this.stock_name = stocks.getStock_name();
        this.stock_symbol = stocks.getStock_symbol();
        this.volume = volume;
        this.type = type;
        this.status = status;
        this.price = price;
        this.remaining_volume = remaining_volume;
    }

    public Order() {

    }
}
