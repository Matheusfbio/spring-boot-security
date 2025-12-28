package br.com.br.com.spring_security.controller;

import br.com.br.com.spring_security.security.JWTCreator;
import br.com.br.com.spring_security.security.JWTObject;
import br.com.br.com.spring_security.security.SecurityConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;

@RestController
public class TestController {
    
    @GetMapping("/test-jwt")
    public String testJWT() {
        try {
            // Criar JWT de teste
            JWTObject jwtObject = new JWTObject();
            jwtObject.setSubject("testuser");
            jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
            jwtObject.setExpiration(new Date(System.currentTimeMillis() + 3600000));
            jwtObject.setRoles(Arrays.asList("USERS", "MANAGERS"));
            
            String token = JWTCreator.create(SecurityConfig.PREFIX, SecurityConfig.KEY, jwtObject);
            System.out.println("Token gerado: " + token);
            
            return "Use este token: " + token;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro: " + e.getMessage();
        }
    }
}