package com.chayxana.chayxana.secret;

import com.chayxana.chayxana.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if(token!=null){
            User user = generateUser(token);
            if(user!=null){
                if (!user.isAccountNonExpired()) {
                    System.err.println("User Expired");
                } else if (!user.isAccountNonLocked()) {
                    System.out.println("User Locked");
                } else if (!user.isCredentialsNonExpired()) {
                    System.out.println("User Credential expired");
                } else if (!user.isEnabled()) {
                    System.out.println("User Disabled");
                } else {
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }


     public User generateUser(String token){
         if (jwtProvider.validateToken(token)) {
          return  jwtProvider.getUserFromToken(token);
         }

         return null;
     }
    public String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return token != null ? token.substring(7) : null;
    }
}
