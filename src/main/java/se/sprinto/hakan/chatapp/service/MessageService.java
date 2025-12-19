package se.sprinto.hakan.chatapp.service;
import se.sprinto.hakan.chatapp.model.User;
import org.springframework.stereotype.Service;
import se.sprinto.hakan.chatapp.model.Message;
import se.sprinto.hakan.chatapp.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    public void save(Message message) {
        repo.save(message);
    }

    public List<Message> getMessages(User user) {
        return user.getMessages();
    }
}

