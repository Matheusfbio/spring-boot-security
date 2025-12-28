package br.com.br.com.spring_security.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

public class JWTCreator {
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ROLES_AUTHORITIES = "Authorities";

    public static String create(String prefix, String key, JWTObject jwtObject) {
        Key secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .setSubject(jwtObject.getSubject()).setIssuedAt(jwtObject.getIssuedAt())
                .setExpiration(jwtObject.getExpiration()).claim(ROLES_AUTHORITIES, jwtObject.getRoles())
                .signWith(secretKey, SignatureAlgorithm.HS512).compact();
        return prefix + " " + token;
    }
    public static JWTObject create(String token, String prefix, String key) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        JWTObject object = new JWTObject();
        
        // Validar se o token não é nulo ou vazio
        if (token == null || token.trim().isEmpty()) {
            throw new MalformedJwtException("Token não pode ser nulo ou vazio");
        }
        
        // Remove o prefixo se ele existir no token
        String cleanToken = token;
        if (prefix != null && token.startsWith(prefix)) {
            cleanToken = token.substring(prefix.length()).trim();
        } else {
            // Fallback: tenta remover "Bearer " se o prefixo configurado não bater ou for nulo
            cleanToken = token.replace("Bearer ", "").trim();
        }
        
        // Validar formato JWT após limpeza
        if (cleanToken.isEmpty() || cleanToken.split("\\.").length != 3) {
            throw new MalformedJwtException("Token não possui formato JWT válido: " + cleanToken);
        }

        Key secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(cleanToken).getBody();
        object.setSubject(claims.getSubject());
        object.setExpiration(claims.getExpiration());
        object.setIssuedAt(claims.getIssuedAt());

        Object rolesObject = claims.get(ROLES_AUTHORITIES);
        System.out.println("JWTCreator: Raw roles object from claims: " + rolesObject);

        List<String> roles = new ArrayList<>();

        if (rolesObject != null) {
            if (rolesObject instanceof List) {
                // Se já é uma lista, processa diretamente
                List<?> rolesList = (List<?>) rolesObject;
                for (Object role : rolesList) {
                    String roleStr = role.toString();
                    // Remove colchetes se existirem
                    roleStr = roleStr.replaceAll("\\[|\\]", "").trim();
                    if (roleStr.contains(",")) {
                        // Se contém vírgula, divide
                        String[] parts = roleStr.split(",");
                        for (String part : parts) {
                            String cleanPart = part.trim();
                            if (!cleanPart.isEmpty()) {
                                roles.add(cleanPart);
                            }
                        }
                    } else if (!roleStr.isEmpty()) {
                        roles.add(roleStr);
                    }
                }
            } else {
                // Se é string, processa como antes
                String rolesStr = rolesObject.toString();
                System.out.println("JWTCreator: Raw roles string: " + rolesStr);
                
                // Remove todos os colchetes externos recursivamente
                while (rolesStr.startsWith("[") && rolesStr.endsWith("]")) {
                    rolesStr = rolesStr.substring(1, rolesStr.length() - 1).trim();
                    System.out.println("JWTCreator: After removing brackets: " + rolesStr);
                }
                
                // Se ainda contém colchetes, é uma string que representa um array
                if (rolesStr.contains("[") && rolesStr.contains("]")) {
                    // Remove colchetes internos
                    rolesStr = rolesStr.replaceAll("\\[|\\]", "");
                    System.out.println("JWTCreator: After removing internal brackets: " + rolesStr);
                }
                
                // Divide por vírgula e limpa espaços
                if (!rolesStr.isEmpty()) {
                    String[] parts = rolesStr.split(",");
                    for (String part : parts) {
                        String cleanPart = part.trim();
                        if (!cleanPart.isEmpty()) {
                            roles.add(cleanPart);
                        }
                    }
                }
            }
        }

        System.out.println("JWTCreator: Parsed roles: " + roles);
        object.setRoles(roles);
        return object;
    }

    private static List<String> checkRoles(List<String> roles) {
        return roles.stream().map(s -> "ROLES_".concat(s.replaceAll("ROLE_", ""))).collect(Collectors.toList());
    }


}
