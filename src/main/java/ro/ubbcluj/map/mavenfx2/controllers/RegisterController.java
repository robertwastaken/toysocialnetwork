package ro.ubbcluj.map.mavenfx2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.mavenfx2.authentication.PasswordAuthentication;
import ro.ubbcluj.map.mavenfx2.authentication.PasswordRepository;
import ro.ubbcluj.map.mavenfx2.domain.User;
import ro.ubbcluj.map.mavenfx2.domain.validators.ValidationException;
import ro.ubbcluj.map.mavenfx2.service.UserService;

import java.util.InputMismatchException;
import java.util.Objects;

public class RegisterController {
    private UserService userService;
    private PasswordRepository passwordRepository;
    PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    PasswordField confirmPasswordTextField;
    @FXML
    Text wrongText;
    @FXML
    Button registerButton;


    public void setUserService(UserService userService){
        this.userService = userService;
    }
    public void setPasswordRepository(PasswordRepository passwordRepository){
        this.passwordRepository = passwordRepository;
    }

    public void register(ActionEvent actionEvent) {
        try{
            if(firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
            usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty())
                return;
            if(wrongText.isVisible())
                return;
            if(passwordRepository.getId(usernameTextField.getText()) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.getDialogPane().setHeaderText("Username is already taken");
                alert.showAndWait();
                return;
            }
            userService.addEntity(new User(firstNameTextField.getText(), lastNameTextField.getText()));
            Long userId = getIdOfUser(firstNameTextField.getText(), lastNameTextField.getText());
            String hashedPassword = passwordAuthentication.hash(passwordTextField.getText().toCharArray());
            passwordRepository.save(usernameTextField.getText(), hashedPassword, userId);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.close();

        }
        catch(ValidationException |IllegalArgumentException| InputMismatchException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    private Long getIdOfUser(String firstName, String lastName) {
        Iterable<User> users = userService.findAll();
        for(User u : users)
            if(Objects.equals(u.getFirstName(), firstName) && Objects.equals(u.getLastName(), lastName))
                return u.getId();
        return null;
    }

    public void comparePasswords(KeyEvent keyEvent) {
        wrongText.setVisible(!Objects.equals(passwordTextField.getText(), confirmPasswordTextField.getText()));
    }
}
