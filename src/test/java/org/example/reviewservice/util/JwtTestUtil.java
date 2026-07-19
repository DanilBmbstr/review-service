package org.example.reviewservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.reviewservice.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTestUtil {


    public final static String TEST_SECRET = "EXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLEEXAMPLE";

    public static String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, TEST_SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}