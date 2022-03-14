package com.example.orderbot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@NoArgsConstructor
public class Wallet {

    private Long id_user;
    private Long id_stock;
    private String stock_name;
    private String stock_symbol;
    private Long volume;

    public Wallet(Users users, Stocks stocks, Long volume){
        this.id_user = users.getId();
        this.id_stock = stocks.getId();
        this.stock_name = stocks.getStock_name();
        this.stock_symbol = stocks.getStock_symbol();
        this.volume = volume;
    }

}

