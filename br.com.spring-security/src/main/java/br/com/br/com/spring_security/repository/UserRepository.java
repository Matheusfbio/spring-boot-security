package br.com.br.com.spring_security.repository;

import br.com.br.com.spring_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.username = (:username)")
    public User findByUsername(@Param("username") String username);
    
    boolean existsByUsername(String username);
}
