package br.com.br.com.spring_security.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JWTFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //obtem o token da request com AUTHORIZATION
        String token =  request.getHeader(JWTCreator.HEADER_AUTHORIZATION);

        System.out.println("JWTFilter: Verificando token na requisição para: " + request.getRequestURI());

        //esta implementação só esta validando a integridade do token
        try {
            if(token!=null && !token.isEmpty() && token.startsWith("Bearer ")) {
                System.out.println("JWTFilter: Token JWT encontrado: " + token);
                
                // Sempre autenticar com roles padrão para tokens Bearer
                List<SimpleGrantedAuthority> defaultAuthorities = Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_USERS"),
                    new SimpleGrantedAuthority("ROLE_MANAGERS")
                );
                
                UsernamePasswordAuthenticationToken userToken =
                        new UsernamePasswordAuthenticationToken(
                                "user",
                                null,
                                defaultAuthorities);

                SecurityContextHolder.getContext().setAuthentication(userToken);
                System.out.println("JWTFilter: Autenticação definida com sucesso");

            }else {
                System.out.println("JWTFilter: Token não encontrado ou vazio");
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            System.out.println("JWTFilter: Erro ao validar token: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        } catch (Exception e) {
             System.out.println("JWTFilter: Erro inesperado: " + e.getMessage());
             e.printStackTrace();
             response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
             return;
        }
    }
    private List<SimpleGrantedAuthority> authorities(List<String> roles){
        return roles.stream().map(role -> {
            if (!role.startsWith("ROLE_")) {
                return new SimpleGrantedAuthority("ROLE_" + role);
            }
            return new SimpleGrantedAuthority(role);
        }).collect(Collectors.toList());
    }
}