package se.sprinto.hakan.chatapp;

import se.sprinto.hakan.chatapp.model.Message;
import se.sprinto.hakan.chatapp.model.User;
import se.sprinto.hakan.chatapp.service.MessageService;
import se.sprinto.hakan.chatapp.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ChatServer server;
    private final UserService userService;
    private final MessageService messageService;

    private PrintWriter out;
    private User user;

    public ClientHandler(Socket socket,
                         ChatServer server,
                         UserService userService,
                         MessageService messageService) {
        this.socket = socket;
        this.server = server;
        this.userService = userService;
        this.messageService = messageService;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {

            this.out = writer;

            writer.println("Välkommen! Har du redan ett konto? (ja/nej)");
            String answer = in.readLine();
            boolean existingUser = false;

            if ("ja".equalsIgnoreCase(answer)) {
                writer.println("Ange användarnamn:");
                String username = in.readLine();

                writer.println("Ange lösenord:");
                String password = in.readLine();

                user = userService.login(username, password);

                if (user == null) {
                    writer.println("Fel användarnamn eller lösenord.");
                    writer.println("Skriv /quit för att avsluta.");
                } else {
                    existingUser = true;
                }

            } else {
                writer.println("Skapa nytt konto. Ange användarnamn:");
                String username = in.readLine();

                writer.println("Ange lösenord:");
                String password = in.readLine();

                user = userService.register(new User(username, password));

                writer.println("Konto skapat. Välkommen, " + user.getUsername() + "!");
            }

            writer.println("Du är inloggad som: " + user.getUsername());

            if (existingUser) {
                List<Message> messageForUser = user.getMessages();
                if (messageForUser != null && !messageForUser.isEmpty()) {
                    writer.println("Din chatthistorik:");
                    for (Message m : messageForUser) {
                        writer.println("[" + m.getTimestamp() + "] " + m.getText());
                    }
                }
            }

            writer.println("Nu kan du börja chatta.");
            writer.println("/mymsgs visar dina meddelanden.");
            writer.println("/quit avslutar sessionen.");

            String msg;
            while ((msg = in.readLine()) != null) {

                if (msg.equalsIgnoreCase("/quit")) {
                    break;

                } else if (msg.equalsIgnoreCase("/mymsgs")) {
                    List<Message> messages = messageService.getMessages(user);
                    if (messages.isEmpty()) {
                        writer.println("Inga meddelanden sparade.");
                    } else {
                        for (Message m : messages) {
                            writer.println("[" + m.getTimestamp() + "] " + m.getText());
                        }
                    }

                } else {
                    server.broadcast(msg, this);
                    messageService.save(new Message(user, msg, LocalDateTime.now()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    public void sendMessage(String msg) {
        if (out != null) out.println(msg);
    }
}
