package br.com.br.com.spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance())    ;
    }
/*  @Bean
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
*/
    @Bean
    protected DefaultSecurityFilterChain SecurityRouter(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize -> authorize.requestMatchers("/").permitAll().requestMatchers(HttpMethod.POST,"/login").permitAll().requestMatchers("/managers").hasRole("MANAGERS").requestMatchers("/users").hasAnyRole("USERS", "MANAGERS").anyRequest().authenticated())).httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
