package org.example.reviewservice.mapper;

import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.dto.response.CreateReviewResponse;
import org.example.reviewservice.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public static CreateReviewResponse toResponse(Review review) {
        return CreateReviewResponse.builder()
                .id(review.getId())
                .text(review.getText())
                .rating(review.getRating())
                .productId(review.getProductId())
                .userId(review.getUserId())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static Review RequestToReview(CreateReviewRequest request, Long userId){
        return Review.builder()
                .text(request.getText())
                .rating(request.getRating())
                .userId(userId)
                .productId(request.getProductId())
                .build();
    }

}
