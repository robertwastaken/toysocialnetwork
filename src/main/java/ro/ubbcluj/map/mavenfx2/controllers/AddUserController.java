package ro.ubbcluj.map.mavenfx2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.ubbcluj.map.mavenfx2.domain.User;

public class AddUserController {

    public static User toAdd = new User("toAdd", "toAdd");

    public static User getToAdd() {
        return toAdd;
    }

    @FXML
    TextField firstNameText;
    @FXML
    TextField lastNameText;
    @FXML
    Button addButton;

    public void addUser(ActionEvent actionEvent) {
        Stage stage = (Stage) addButton.getScene().getWindow();
        toAdd.setFirstName(firstNameText.getText());
        toAdd.setLastName(lastNameText.getText());
        stage.close();
    }
}
