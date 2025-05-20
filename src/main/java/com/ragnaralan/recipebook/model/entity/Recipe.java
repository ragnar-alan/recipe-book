package com.ragnaralan.recipebook.model.entity;

import com.ragnaralan.recipebook.model.Ingredient;
import com.ragnaralan.recipebook.model.MealType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "recipes")
@Entity
public class Recipe {

    @Id
    @GeneratedValue(generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", allocationSize = 1)
    private Long id;

    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Ingredient> ingredients;

    private Integer cookingTimeInMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_meal")
    private MealType type;

    @CreatedDate
    private Timestamp createdAt;
}
