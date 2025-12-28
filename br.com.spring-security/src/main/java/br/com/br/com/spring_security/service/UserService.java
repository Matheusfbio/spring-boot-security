package br.com.br.com.spring_security.service;

import br.com.br.com.spring_security.model.User;
import br.com.br.com.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;
    public void createUser(User user) {
        String pass = user.getPassword();

        user.setPassword(encoder.encode(pass));
        repository.save(user);
    }
}
