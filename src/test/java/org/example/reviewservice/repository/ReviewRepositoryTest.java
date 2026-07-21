package org.example.reviewservice.repository;

import org.example.reviewservice.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void save_shouldPersistReview() {

        Review review = Review.builder()
                .userId(1L)
                .productId(1L)
                .text("Review text")
                .rating(5)
                .build();


        Review saved = reviewRepository.save(review);


        assertNotNull(saved.getId());

        Review found = reviewRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Review text", found.getText());
        assertEquals(5, found.getRating());
    }


    @Test
    public void findById_shouldReturnReview_whenExists() {

        Review review = Review.builder()
                .userId(1L)
                .productId(1L)
                .text("Review text")
                .rating(5)
                .build();
        Long id = entityManager.persistAndGetId(review, Long.class);
        entityManager.flush();

        Optional<Review> found = reviewRepository.findById(id);

        assertTrue(found.isPresent());
        assertEquals("Review text", found.get().getText());
    }
}
