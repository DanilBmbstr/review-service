package org.example.reviewservice.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.reviewservice.client.ProductServiceClient;
import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.entity.Review;
import org.example.reviewservice.exception.ProductNotExistsException;
import org.example.reviewservice.mapper.ReviewMapper;
import org.example.reviewservice.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository mockReviewRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private KafkaProducerService kafkaProducerService;
    @Mock
    private ProductServiceClient productServiceClient;

    private Long UserId = (long)1;

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


    }

    @Test
    public void createReview_calls_save_OnlyOneTime(){

        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(5);
        request.setProductId(1);

        when(mockReviewRepository.save(any(Review.class))).thenReturn(ReviewMapper.RequestToReview(request, UserId));

        when(productServiceClient.productExists(any(Long.class))).thenReturn(true);

        Review result = reviewService.createReview(request,UserId);
        verify(mockReviewRepository, times(1)).save(any(Review.class));
    }


    @Test
    public void createReview_non_existing_product_shouldThrow_ProductNotExist() {

        Long UserId = (long)1;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setText("Review Text");
        request.setRating(5);
        request.setProductId(1);


        when(productServiceClient.productExists(any(Long.class))).thenReturn(false);



        assertThrows(ProductNotExistsException.class, ()->{Review result = reviewService.createReview(request,UserId);});

    }


}

