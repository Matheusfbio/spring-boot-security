package br.com.br.com.spring_security.init;

import br.com.br.com.spring_security.model.User;
import br.com.br.com.spring_security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartApplication implements CommandLineRunner {
    @Autowired
    private UserRepository repository;
    
    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        User user = repository.findByUsername("admin");
            if (user == null) {
                user = new User();
                user.setName("ADMIN");
                user.setUsername("admin");
                user.setPassword(encoder.encode("master123"));
                user.getRoles().add("MANAGERS");
                repository.save(user);
                System.out.println("ADMIN user created successfully");
            }
            
            User simpleUser = repository.findByUsername("user");
            if (simpleUser == null) {
                simpleUser = new User();
                simpleUser.setName("USER");
                simpleUser.setUsername("user");
                simpleUser.setPassword(encoder.encode("user123"));
                simpleUser.getRoles().add("USERS");
                repository.save(simpleUser);
                System.out.println("USER user created successfully");
            }
    }
}
