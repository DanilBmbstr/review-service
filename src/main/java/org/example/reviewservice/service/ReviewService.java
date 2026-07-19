package org.example.reviewservice.service;

import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.entity.Review;
import org.example.reviewservice.exception.IllegalRatingException;
import org.example.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review createReview(CreateReviewRequest review, long userId) throws IllegalArgumentException{

        if(review.getRating() < 1 || review.getRating() > 10) {
            throw new IllegalRatingException("Rating should be between 1 and 10");
        }

        Review mappedReview = new Review();
        mappedReview.setUserId(userId);
        mappedReview.setText(review.getText());
        mappedReview.setRating(review.getRating());
        mappedReview.setProductId(review.getProductId());
        return reviewRepository.save(mappedReview);
    }



}