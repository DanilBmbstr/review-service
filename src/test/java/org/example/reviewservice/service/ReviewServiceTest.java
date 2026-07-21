package org.example.reviewservice.service;



import org.example.reviewservice.client.ProductServiceClient;
import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.entity.Review;
import org.example.reviewservice.mapper.ReviewMapper;
import org.example.reviewservice.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository mockReviewRepository;

    @Mock
    private ProductServiceClient productServiceClient;



    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void getAllReviews_shouldReturnList() {
        when(mockReviewRepository.findAll()).thenReturn(new ArrayList<>());

        List<Review> result = reviewService.getAllReviews();
        assertInstanceOf(ArrayList.class, result);
    }


    @Test
    public void createReview_correct_input_shouldReturnReview() {

        Long UserId = (long)1;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setText("Review Text");
        request.setRating(5);
        request.setProductId(1);

        when(mockReviewRepository.save(any(Review.class))).thenReturn(ReviewMapper.RequestToReview(request, UserId));

        when(productServiceClient.productExists(any(Long.class))).thenReturn(true);

        Review result = reviewService.createReview(request,UserId);

        assertEquals(UserId, result.getUserId());
        assertEquals(request.getText(), result.getText());
        assertEquals(request.getProductId(), result.getProductId());
        assertEquals(request.getRating(), result.getRating());
        verify(mockReviewRepository, times(1)).save(any(Review.class));

    }
}

