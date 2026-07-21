package org.example.reviewservice.controller;


import org.example.reviewservice.config.SecurityConfig;
import org.example.reviewservice.dto.request.CreateReviewRequest;
import org.example.reviewservice.entity.Review;
import org.example.reviewservice.exception.ProductNotExistsException;
import org.example.reviewservice.filter.JwtAuthenticationFilter;
import org.example.reviewservice.mapper.ReviewMapper;
import org.example.reviewservice.service.ReviewService;
import org.example.reviewservice.util.JwtTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ReviewController.class)
@Import({ReviewMapper.class, SecurityConfig.class, JwtAuthenticationFilter.class})
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ReviewService mockReviewService;


    public Long userId = (long)1;


    private final String TEST_KEY = "EXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLE";
    @ParameterizedTest
    @CsvSource({ "54927", "-11111111","456"})
    public void getReviews_ValidToken_shouldReturnList(String userId) throws Exception{
        when(mockReviewService.getAllReviews()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/reviews").header("Authorization","Bearer " + JwtTestUtil.generateToken(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    public void getReviews_InvalidToken_shouldReturn403() throws Exception{
        when(mockReviewService.getAllReviews()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/reviews").header("Authorization","Bearer invalid_token"))
                .andExpect(status().isForbidden());

    }


    @Test
    public void getReviews_InvalidSub_shouldReturn401() throws Exception{
        when(mockReviewService.getAllReviews()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/reviews").header("Authorization",
                        "Bearer " + JwtTestUtil.generateToken("STRING")))
                .andExpect(status().isUnauthorized());

    }





    @Test
    public void createReview_validInput_shouldReturnCreated() throws Exception{




        Review savedReview = new Review();
        savedReview.setId(userId);
        savedReview.setText("reviewText");
        savedReview.setRating(1);
        savedReview.setProductId((long)1);
        savedReview.setUserId((long)1);
        when(mockReviewService.createReview(any(CreateReviewRequest.class), eq(userId))).thenReturn(savedReview);



        String json = "{\"text\":\"reviewText\",\"rating\":1,\"productId\":1}";
        mockMvc.perform(post("/api/reviews").header("Authorization",
                        "Bearer " +  JwtTestUtil.generateToken(userId.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.productId").value("1"))
                .andExpect(jsonPath("$.rating").value("1"))
                .andExpect(jsonPath("$.text").value("reviewText"));



        verify(mockReviewService, times(1)).createReview(any(CreateReviewRequest.class), eq(userId));


    }




    @Test
    public void createReview_InvalidToken_shouldReturn403() throws Exception{
        String json = "{\"text\":\"reviewText\",\"rating\":1,\"productId\":1}";
        mockMvc.perform(post("/api/reviews").header("Authorization",
                                "Bearer invalid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());

    }

    @Test
    public void createReview_InvalidSubject_shouldReturn403() throws Exception{
        String json = "{\"text\":\"reviewText\",\"rating\":1,\"productId\":1}";
        mockMvc.perform(post("/api/reviews").header("Authorization",
                                "Bearer " + JwtTestUtil.generateToken("STRING"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void createReview_Rating_Less_Than_1_shouldReturnBadRequest() throws Exception{

        String json = "{\"text\":\"reviewText\",\"rating\":0,\"productId\":1}";
        mockMvc.perform(post("/api/reviews").header("Authorization",
                                "Bearer " +  JwtTestUtil.generateToken(userId.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());



        verify(mockReviewService, times(0)).createReview(any(CreateReviewRequest.class), eq(userId));


    }


    @Test
    public void createReview_Rating_More_Than_10_shouldReturnBadRequest() throws Exception{

        String json = "{\"text\":\"reviewText\",\"rating\":11,\"productId\":1}";
        mockMvc.perform(post("/api/reviews").header("Authorization",
                                "Bearer " +  JwtTestUtil.generateToken(userId.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());



        verify(mockReviewService, times(0)).createReview(any(CreateReviewRequest.class), eq(userId));


    }

    @Test
    public void createReview_ProductNotExists_shouldReturn404() throws Exception{
        when(mockReviewService.createReview(any(CreateReviewRequest.class), eq(userId))).thenThrow(ProductNotExistsException.class);
        String json = "{\"text\":\"reviewText\",\"rating\":5,\"productId\":555}";
        mockMvc.perform(post("/api/reviews").header("Authorization",
                                "Bearer " +  JwtTestUtil.generateToken(userId.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

    }

}
