package com.ragnaralan.recipebook.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class IngredientDto {
    @NotEmpty
    private String name;
    @NotNull
    private BigDecimal amount;

    private String unit;
}
