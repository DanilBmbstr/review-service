package org.example.reviewservice.client;

import org.example.reviewservice.dto.ProductExistsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("prod")
public class ProductServiceClientReal implements ProductServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${product.service.url}")
    private String productServiceUrl;



    @Override
    public boolean productExists(Long productId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = productServiceUrl + "/api/products/" + productId + "/exists";
        try {
            ProductExistsResponse response = restTemplate.getForObject(url, ProductExistsResponse.class);
            return response != null && response.isExists();
        }
        catch (Exception ex){
        return false;
        }

    }
}