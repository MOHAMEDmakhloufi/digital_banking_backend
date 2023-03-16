package com.fsb.digital_banking_backend.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if(httpServletRequest.getServletPath().contains("/refreshToken")||httpServletRequest.getServletPath().contains("/login")){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }else {
            String authorizationToken= httpServletRequest.getHeader("Authorization");

            if(authorizationToken!=null && authorizationToken.startsWith("Bearer ")){
                try{
                    String jwt= authorizationToken.substring(7);

                    Algorithm algorithm= Algorithm.HMAC256("mySecretBank");
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(jwt);

                    String username= decodedJWT.getSubject();
                    String[] roles= decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<GrantedAuthority> authorityCollection= new ArrayList<>();
                    for( String r:roles)
                        authorityCollection.add(new SimpleGrantedAuthority(r));

                    UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(username, null, authorityCollection);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                }catch (Exception e){

                    httpServletResponse.setHeader("error-message", e.getMessage());
                    httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);

                }
            }else {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        }
    }
}

