package org.example.reviewservice.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(       check = @CheckConstraint(
        name = "rating_range_constraint",
        constraint = "rating >= 0 AND rating <= 10"
    ))
    private int rating;


    @Column(length = 2500)
    private String text;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
