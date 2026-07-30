package org.example.reviewservice.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.SerializationException;
import lombok.RequiredArgsConstructor;
import org.example.reviewservice.client.ProductServiceClient;
import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.entity.Review;

import org.example.reviewservice.exception.KafkaProducerException;
import org.example.reviewservice.exception.ProductNotExistsException;
import org.example.reviewservice.mapper.ReviewMapper;
import org.example.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor //Эта аннотация создаёт конструкторы для final параметров
@Service
public class ReviewService {
    private final KafkaProducerService producerService;
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private ReviewRepository reviewRepository;

    private final ObjectMapper objectMapper;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review createReview(CreateReviewRequest review, long userId) throws SerializationException, KafkaProducerException{



    if(productServiceClient.productExists(review.getProductId())) {
        Review mappedReview = ReviewMapper.RequestToReview(review, userId);




        Review saved =  reviewRepository.save(mappedReview);


try {
    String kafkaMessage = objectMapper.writeValueAsString(saved);
    producerService.sendMessage(kafkaMessage);
}
    catch (IOException ex){
        throw new SerializationException("Failed to serialize review", ex);
    }
catch (IllegalArgumentException ex) {
    throw new KafkaProducerException(ex.getMessage());
}

        return saved;


    }
    else {throw new ProductNotExistsException("Product id:" + review.getProductId());}
    }



}