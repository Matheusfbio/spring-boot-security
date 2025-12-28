package br.com.br.com.spring_security.test;

import br.com.br.com.spring_security.security.JWTCreator;
import br.com.br.com.spring_security.security.JWTObject;
import br.com.br.com.spring_security.security.SecurityConfig;

import java.util.Arrays;
import java.util.Date;

public class JWTTest {
    public static void main(String[] args) {
        try {
            // Simular configuração
            SecurityConfig.PREFIX = "Bearer";
            SecurityConfig.KEY = "SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY";
            
            // Criar JWT
            JWTObject jwtObject = new JWTObject();
            jwtObject.setSubject("testuser");
            jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
            jwtObject.setExpiration(new Date(System.currentTimeMillis() + 3600000));
            jwtObject.setRoles(Arrays.asList("USERS", "MANAGERS"));
            
            String token = JWTCreator.create(SecurityConfig.PREFIX, SecurityConfig.KEY, jwtObject);
            System.out.println("Token gerado: " + token);
            
            // Validar JWT
            JWTObject decoded = JWTCreator.create(token, SecurityConfig.PREFIX, SecurityConfig.KEY);
            System.out.println("Subject: " + decoded.getSubject());
            System.out.println("Roles: " + decoded.getRoles());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}