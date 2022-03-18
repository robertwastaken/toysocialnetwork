package ro.ubbcluj.map.mavenfx2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.ubbcluj.map.mavenfx2.authentication.PasswordRepository;
import ro.ubbcluj.map.mavenfx2.controllers.HomeController;
import ro.ubbcluj.map.mavenfx2.controllers.LoginController;
import ro.ubbcluj.map.mavenfx2.domain.validators.FriendRequestValidator;
import ro.ubbcluj.map.mavenfx2.domain.validators.FriendshipValidator;
import ro.ubbcluj.map.mavenfx2.domain.validators.MessageValidator;
import ro.ubbcluj.map.mavenfx2.domain.validators.UserValidator;
import ro.ubbcluj.map.mavenfx2.repository.FriendRequestRepository;
import ro.ubbcluj.map.mavenfx2.repository.FriendshipRepository;
import ro.ubbcluj.map.mavenfx2.repository.MessageRepository;
import ro.ubbcluj.map.mavenfx2.repository.UserRepository;
import ro.ubbcluj.map.mavenfx2.service.*;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String url = "jdbc:postgresql://localhost:5432/lab6";
        String username = "postgres";
        String password = "admin";

        UserValidator userValidator = new UserValidator();
        UserRepository userRepository = new UserRepository(url, username, password, userValidator);
        UserService userService = new UserService(userRepository);

        FriendshipValidator friendshipValidator = new FriendshipValidator(userRepository);
        FriendshipRepository friendshipRepository = new FriendshipRepository(url, username, password, friendshipValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);

        Network network = new Network(userRepository, friendshipRepository);

        MessageValidator messageValidator = new MessageValidator(userRepository);
        MessageRepository messageRepository = new MessageRepository(url, username, password, messageValidator);
        MessageService messageService = new MessageService(messageRepository);

        FriendRequestValidator friendRequestValidator = new FriendRequestValidator(userRepository);
        FriendRequestRepository friendRequestRepository = new FriendRequestRepository(url, username, password, friendRequestValidator);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);

        PasswordRepository passwordRepository = new PasswordRepository(url, username, password);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene home = new Scene(fxmlLoader.load(), 346, 276);
        stage.setTitle("Login");
        stage.setScene(home);
        LoginController loginController = fxmlLoader.getController();
        loginController.setUserService(userService);
        loginController.setFriendshipService(friendshipService);
        loginController.setFriendRequestService(friendRequestService);
        loginController.setMessageService(messageService);
        loginController.setPasswordRepository(passwordRepository);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}