package com.example.orderbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Stocks {

    private Long id;
    private String stock_name;
    private String stock_symbol;
}
