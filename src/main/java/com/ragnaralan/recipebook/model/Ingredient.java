package com.ragnaralan.recipebook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Ingredient {
    private String name;
    private BigDecimal amount;
    private String unit;
}
