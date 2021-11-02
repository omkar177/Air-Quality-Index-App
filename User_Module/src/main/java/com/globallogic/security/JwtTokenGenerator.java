package com.globallogic.security;

import com.globallogic.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenGenerator implements Serializable {

    private static final long serialVersionUid= -2550185165626007488L;

    public static final long JwtTokenValidity=5*60;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user){
        Map<String,Object> claims=new HashMap<>();
        return doGenerateToken(claims,user.getName());

    }

    private String doGenerateToken(Map<String, Object> claims, String name) {
        return Jwts.builder().setClaims(claims).setSubject(name).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JwtTokenValidity*100))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }

}
