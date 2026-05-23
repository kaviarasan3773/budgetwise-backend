package com.kaviarasan.budgetwise.security;

import javax.crypto.SecretKey;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(
                    "mysecretkeymysecretkeymysecretkey12"
                            .getBytes());

    public String generateToken(String email) {

        return Jwts.builder()  // start building JWTS
                .setSubject(email) 	// stores user entity,owner of this token
                .setIssuedAt(new Date()) // issue date
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60))  // expire date
                .signWith(secretKey) // secretkey-> add digital signature
                .compact();	// converts jwt object to string
    }
    
    public String extractEmail(String token) {

        return Jwts.parserBuilder() 	// starts tokenreading process
                .setSigningKey(secretKey) // verify digital signature same key as creation
                .build()	// convert string to jwt object
                .parseClaimsJws(token) // actually read jwt datas
                .getBody() 		// gets payload/claims
                .getSubject(); // get subject while creation
    }
    
    public boolean validateToken(String token,String email) {
    	String extractedEmail = extractEmail(token);
    	return extractedEmail.equals(email) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}