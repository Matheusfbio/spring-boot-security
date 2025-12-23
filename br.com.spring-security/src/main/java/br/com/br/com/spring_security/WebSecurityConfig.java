package br.com.br.com.spring_security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}user123")
                .roles("USERS")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}master123")
                .roles("MANAGERS", "USERS")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    protected DefaultSecurityFilterChain SecurityRouter(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize -> authorize.requestMatchers("/").permitAll().requestMatchers(HttpMethod.POST,"/login").permitAll().requestMatchers("/managers").hasRole("MANAGERS").requestMatchers("/users").hasAnyRole("USERS", "MANAGERS").anyRequest().authenticated())).formLogin(Customizer.withDefaults());
        return http.build();
    }
}
