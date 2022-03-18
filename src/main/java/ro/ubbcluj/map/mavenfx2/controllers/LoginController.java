package ro.ubbcluj.map.mavenfx2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ro.ubbcluj.map.mavenfx2.HelloApplication;
import ro.ubbcluj.map.mavenfx2.authentication.PasswordRepository;
import ro.ubbcluj.map.mavenfx2.domain.User;
import ro.ubbcluj.map.mavenfx2.service.FriendRequestService;
import ro.ubbcluj.map.mavenfx2.service.FriendshipService;
import ro.ubbcluj.map.mavenfx2.service.MessageService;
import ro.ubbcluj.map.mavenfx2.service.UserService;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    private UserService userService;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;
    private MessageService messageService;
    private PasswordRepository passwordRepository;

    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    Text wrongText;
    @FXML
    Button loginButton;
    @FXML
    Button registerButton;

    public void setUserService(UserService userService){
        this.userService = userService;
    }
    public void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }
    public void setFriendRequestService(FriendRequestService friendRequestService){
        this.friendRequestService = friendRequestService;
    }
    public void setMessageService(MessageService messageService){
        this.messageService = messageService;
    }
    public void setPasswordRepository(PasswordRepository passwordRepository){
        this.passwordRepository = passwordRepository;
    }

    public void login(ActionEvent actionEvent) throws IOException {
        if (!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty())
            if (passwordRepository.authenticate(usernameTextField.getText(), passwordTextField.getText())){
                if(Objects.equals(usernameTextField.getText(), "admin")){
                    Stage window = new Stage();

                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
                    Scene home = new Scene(fxmlLoader.load(), 600, 400);
                    HomeController homeController = fxmlLoader.getController();
                    homeController.setUserService(userService);
                    homeController.setFriendshipService(friendshipService);
                    homeController.setFriendRequestService(friendRequestService);
                    homeController.setMessageService(messageService);
                    window.setTitle("Home");
                    window.setScene(home);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();
                    window.show();
                }
                else{
                    User user = userService.findOne(passwordRepository.getId(usernameTextField.getText()));

                    Stage window = new Stage();

                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friends.fxml"));
                    Scene friends = new Scene(fxmlLoader.load(), 800, 400);
                    FriendController friendController = fxmlLoader.getController();
                    friendController.setFriendshipService(friendshipService);
                    friendController.setUserService(userService);
                    friendController.setFriendRequestService(friendRequestService);
                    friendController.setMessageService(messageService);
                    friendController.setPasswordRepository(passwordRepository);
                    friendController.setId(user.getId());
                    window.setTitle(user.getFirstName() + " " + user.getLastName());
                    window.setScene(friends);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();
                    window.show();
                }
            }
            else{
                wrongText.setVisible(true);
                passwordTextField.clear();
            }
    }

    public void register(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene addUser = new Scene(fxmlLoader.load(), 330, 377);
        RegisterController registerController = fxmlLoader.getController();
        registerController.setUserService(userService);
        registerController.setPasswordRepository(passwordRepository);
        window.setTitle("Register");
        window.setScene(addUser);
        window.showAndWait();
    }
}
