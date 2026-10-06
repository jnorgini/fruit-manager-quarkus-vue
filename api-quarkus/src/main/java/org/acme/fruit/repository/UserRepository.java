package org.acme.fruit.repository; 

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.fruit.domain.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }
}
