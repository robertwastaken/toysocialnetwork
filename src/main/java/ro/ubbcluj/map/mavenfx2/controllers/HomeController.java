package ro.ubbcluj.map.mavenfx2.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ro.ubbcluj.map.mavenfx2.HelloApplication;
import ro.ubbcluj.map.mavenfx2.domain.User;
import ro.ubbcluj.map.mavenfx2.domain.validators.ValidationException;
import ro.ubbcluj.map.mavenfx2.service.FriendRequestService;
import ro.ubbcluj.map.mavenfx2.service.FriendshipService;
import ro.ubbcluj.map.mavenfx2.service.MessageService;
import ro.ubbcluj.map.mavenfx2.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomeController implements Initializable {

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;
    private MessageService messageService;
    ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    TableView<User> userTable;
    @FXML
    TableColumn<User, Long> userTableId;
    @FXML
    TableColumn<User, String> userTableFirstName;
    @FXML
    TableColumn<User, String> userTableLastName;
    @FXML
    Button addButton;
    @FXML
    Button deleteButton;
    @FXML
    Button selectButton;

    public HomeController() {
    }

    public void setUserService(UserService userService){
        this.userService = userService;
        loadUsers();
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

    private void loadUsers() {
        Iterable<User> usersIterable = userService.findAll();
        List<User> usersList = StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());
        users.setAll(usersList);
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTableId.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        userTableFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        userTableLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        userTable.setItems(users);
    }

    private void refresh() {
        loadUsers();
        userTable.setItems(users);
    }

    public void addUserDialog(ActionEvent actionEvent) throws IOException {

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addUser.fxml"));
        Scene addUser = new Scene(fxmlLoader.load(), 300, 200);
        window.setTitle("Add User");
        window.setScene(addUser);
        window.showAndWait();

        try {
            userService.addEntity(AddUserController.getToAdd());
        }catch(ValidationException |IllegalArgumentException| InputMismatchException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText(e.getMessage());
            alert.showAndWait();
        }

        refresh();
    }

    private boolean checkSelection(){
        User selected = userTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText("N-ai selectat nimic");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void deleteUser(ActionEvent actionEvent) {

        if(!checkSelection())
            return;
        userService.deleteEntity(userTable.getSelectionModel().getSelectedItem().getId());
        refresh();
    }

    public void selectUser(ActionEvent actionEvent) throws IOException {
        if(!checkSelection())
            return;

        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friends.fxml"));
        Scene friends = new Scene(fxmlLoader.load(), 800, 400);
        FriendController friendController = fxmlLoader.getController();
        friendController.setFriendshipService(friendshipService);
        friendController.setUserService(userService);
        friendController.setFriendRequestService(friendRequestService);
        friendController.setMessageService(messageService);
        friendController.setId(selectedUser.getId());
        friendController.setAdmin();
        window.setTitle(selectedUser.getFirstName() + " " + selectedUser.getLastName());
        window.setScene(friends);
        window.showAndWait();
    }

    public void requestDialog(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friendRequests.fxml"));
        Scene friendRequests = new Scene(fxmlLoader.load(), 800, 400);
        FriendRequestController friendRequestController = fxmlLoader.getController();
        friendRequestController.setFriendshipService(friendshipService);
        friendRequestController.setFriendRequestService(friendRequestService);
        friendRequestController.setUserService(userService);
        window.setTitle("Friend Requests Management");
        window.setScene(friendRequests);
        window.showAndWait();
    }
}
