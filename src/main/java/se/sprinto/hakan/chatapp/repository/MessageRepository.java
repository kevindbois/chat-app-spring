package se.sprinto.hakan.chatapp.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import se.sprinto.hakan.chatapp.model.Message;


public interface MessageRepository extends JpaRepository <Message, Long> {

}


