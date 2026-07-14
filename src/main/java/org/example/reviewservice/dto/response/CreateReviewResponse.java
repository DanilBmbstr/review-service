package org.example.reviewservice.dto.response;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class CreateReviewResponse {
    private Long id;
    private String text;
    private int rating;
    private long productId;
    private long userId;
    private LocalDateTime createdAt;
}
