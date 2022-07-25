package com.chayxana.chayxana.secret;

import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.repo.UserRepo;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Component
public class JwtProvider {
    @Value(value = "${jwt.secretKey}")
    private String secretKey;

    @Value(value = "${jwt.expireDateInMilliSecund}")
    private Long expirationDateInMilliSecond;

    @Autowired
    UserRepo userRepo;

    public String generateToken(User user) {
        Date issueDate = new Date();
        Date expireDate = new Date(issueDate.getTime()+expirationDateInMilliSecond);
        return Jwts
                .builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(issueDate)
                .claim("role",user.getRole().getRoleName())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }
    public boolean validateToken(String token){
          try {
              Jwts
                      .parser()
                      .setSigningKey(secretKey)
                      .parseClaimsJws(token);
              return true;
          }
          catch (
                  MalformedJwtException malformedJwtException) {
              System.err.println("Buzilgan token");
          } catch (
                  SignatureException s) {
              System.err.println("Kalit so'z xato");
          } catch (
                  UnsupportedJwtException unsupportedJwtException) {
              System.err.println("Qo'llanilmagan token");
          } catch (IllegalArgumentException ex) {
              System.err.println("Bo'sh token");
          }
        return false;
    }

    public User getUserFromToken(String token){
        String id = Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Optional<User> optionalUser = userRepo.findById(UUID.fromString(id));
        return optionalUser.orElseGet(null);
    }
}
