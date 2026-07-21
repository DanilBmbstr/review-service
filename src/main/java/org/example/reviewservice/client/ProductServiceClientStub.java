package org.example.reviewservice.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class ProductServiceClientStub implements ProductServiceClient{

    @Override
    public boolean productExists(Long productId) {
        return true;
    }
}
