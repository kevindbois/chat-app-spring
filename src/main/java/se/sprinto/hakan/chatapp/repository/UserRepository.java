package se.sprinto.hakan.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.sprinto.hakan.chatapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.messages
        WHERE u.username = :username AND u.password = :password
    """)
    User findByUsernameAndPassword(String username, String password);
}