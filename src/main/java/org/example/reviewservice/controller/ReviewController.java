package org.example.reviewservice.controller;

import jakarta.validation.Valid;
import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.dto.response.CreateReviewResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @PostMapping
    public ResponseEntity<CreateReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest review,
                                                             Authentication authentication)
    {
        String userIdString = authentication.getPrincipal().toString();
        Long userId = Long.parseLong(userIdString);
        return ResponseEntity
                .status(HttpStatus.CREATED).body(null);
                //.header("Location", "/api/users/" + saved.getId())
                //.body(saved);


    }
}
