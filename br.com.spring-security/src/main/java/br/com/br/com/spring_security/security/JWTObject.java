package br.com.br.com.spring_security.security;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class JWTObject {
    private String subject;
    private Date issuedAt;
    private Date expiration;
    private List<String> roles;


    public void setRoles(List<String> roles){
        this.roles = Arrays.asList(roles.toString());
    }
}
