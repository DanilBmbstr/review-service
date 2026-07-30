package org.example.reviewservice.controller;

import jakarta.validation.Valid;
import lombok.Builder;
import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.dto.response.CreateReviewResponse;
import org.example.reviewservice.entity.Review;
import org.example.reviewservice.mapper.ReviewMapper;
import org.example.reviewservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
@Builder
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<CreateReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest review,
                                                             Authentication authentication)
    {
        String userIdString = authentication.getPrincipal().toString();
        Long userId = Long.parseLong(userIdString);

        Review saved = reviewService.createReview(review, userId);

        CreateReviewResponse response = reviewMapper.toResponse(saved);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/reviews/" + saved.getId())
                .body(response);


    }


    @GetMapping
    public ResponseEntity<List<CreateReviewResponse>> getReview(){
        List<Review> unmappedResult = reviewService.getAllReviews();
        List<CreateReviewResponse> result = new ArrayList<>();
        for(Review review : unmappedResult){
            CreateReviewResponse mapped = reviewMapper.toResponse(review);

            result.add(mapped);
        }

        return ResponseEntity.ok(result);
    }
}
