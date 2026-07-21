package org.example.reviewservice.service;

import org.example.reviewservice.client.ProductServiceClient;
import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.entity.Review;
import org.example.reviewservice.exception.ProductNotExistsException;
import org.example.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private ReviewRepository reviewRepository;
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review createReview(CreateReviewRequest review, long userId) throws ProductNotExistsException{

    if(productServiceClient.productExists(review.getProductId())) {
        Review mappedReview = new Review();
        mappedReview.setUserId(userId);
        mappedReview.setText(review.getText());
        mappedReview.setRating(review.getRating());
        mappedReview.setProductId(review.getProductId());
        return reviewRepository.save(mappedReview);
    }
    else {throw new ProductNotExistsException("Product id:" + review.getProductId());}
    }



}