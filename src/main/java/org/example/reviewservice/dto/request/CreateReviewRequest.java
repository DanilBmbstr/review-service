package org.example.reviewservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {


    @NotBlank
    private String text;

    @Min(1)
    @Max(10)
    @NotNull
    private Integer rating;


    @Min(1)
    @NotNull
    private long productId;

    // Getters and setters
}