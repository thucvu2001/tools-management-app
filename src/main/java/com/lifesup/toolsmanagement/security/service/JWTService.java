package com.lifesup.toolsmanagement.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lifesup.toolsmanagement.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTService {
    private static final String SECRET_KEY = "c342dd5ff691b05ce02f7d0dd292a65eb4d89965";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
    private static final Instant expirationTime = Instant.now().plusSeconds(86400);

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(expirationTime))
                .sign(algorithm);
    }
}
