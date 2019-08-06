package com.yida.modules.yde.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenService {
    @Value("${token.secretKey}")
    private String secretKey;

    public String generateToken(String subject){
        String jws = Jwts.builder().setSubject(subject).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, secretKey).compact();
        return jws;
    }

    public String getSubject(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Date getIssuedAt(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getIssuedAt();
    }

    public Claims getClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
