package com.Kodakdev.KodakHotel.security;

import com.Kodakdev.KodakHotel.utils.JWTConstants;
import com.Kodakdev.KodakHotel.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CachingUserDetailsService cachingUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader(JWTConstants.HEADER_STRING);
        String username = null;
        String token = null;

        if(requestHeader != null && requestHeader.startsWith(JWTConstants.TOKEN_PREFIX)) {
            token = requestHeader.substring(7);

            try {
                username = this.jwtUtils.getUsernameFormToken(token);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid header value");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // fetch user details from username
            UserDetails userDetails = cachingUserDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtUtils.validateToken(token, userDetails);

            if(validateToken) {
                // set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Validation Failed!!!");
            }
        }

        filterChain.doFilter(request, response);
    }
}
