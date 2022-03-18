package ro.ubbcluj.map.mavenfx2.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.mavenfx2.domain.FriendRequest;
import ro.ubbcluj.map.mavenfx2.domain.Friendship;
import ro.ubbcluj.map.mavenfx2.domain.User;
import ro.ubbcluj.map.mavenfx2.domain.validators.ValidationException;
import ro.ubbcluj.map.mavenfx2.service.FriendRequestService;
import ro.ubbcluj.map.mavenfx2.service.FriendshipService;
import ro.ubbcluj.map.mavenfx2.service.UserService;

import java.net.URL;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SendRequestController implements Initializable {

    private Long from;
    private UserService userService;
    private FriendRequestService friendRequestService;
    private FriendshipService friendshipService;
    ObservableList<User> users = FXCollections.observableArrayList();
    ObservableList<Friendship> friendships = FXCollections.observableArrayList();
    ObservableList<User> foundUsers = FXCollections.observableArrayList();

    @FXML
    TableView<User> userTable;
    @FXML
    TableColumn<User, String> userTableFirstName;
    @FXML
    TableColumn<User, String> userTableLastName;
    @FXML
    TextField searchTextField;
    @FXML
    Button doneButton;
    @FXML
    Button sendButton;

    public void setUserService(UserService userService){
        this.userService = userService;
        loadUsers();
    }

    public void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
        loadFriendships();
        removeFriendsFromList();
        removeFriendRequestsFromList();
    }

    public void setFriendRequestService(FriendRequestService friendRequestService){
        this.friendRequestService = friendRequestService;
    }

    public void setFrom(Long from){
        this.from = from;
    }

    private void loadUsers() {
        Iterable<User> usersIterable = userService.findAll();
        List<User> usersList = StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());
        users.setAll(usersList);
    }

    private void loadFriendships() {
        Iterable<Friendship> friendshipsIterable = friendshipService.findAll();
        List<Friendship> friendshipsList = StreamSupport.stream(friendshipsIterable.spliterator(), false)
                .collect(Collectors.toList());
        friendships.setAll(friendshipsList);
    }

    private void removeFriendsFromList() {
        List<User> friends= new ArrayList<User>();
        for(Friendship f : friendships){
            if(Objects.equals(f.getId1(), from)){
                User friend = userService.findOne(f.getId2());
                friends.add(friend);
            }
            else if(Objects.equals(f.getId2(), from)){
                User friend = userService.findOne(f.getId1());
                friends.add(friend);
            }
        }
        users.removeAll(friends);
    }

    private void removeFriendRequestsFromList() {
        List<User> friends= new ArrayList<User>();
        for(FriendRequest f : friendRequestService.findAll()){
            if(!Objects.equals(f.getStatus(), "pending"))
                continue;
            if(Objects.equals(f.getFrom(), from)){
                User friend = userService.findOne(f.getTo());
                friends.add(friend);
            }
            else if(Objects.equals(f.getTo(), from)){
                User friend = userService.findOne(f.getFrom());
                friends.add(friend);
            }
        }
        users.removeAll(friends);
    }

    public void sendRequest(ActionEvent actionEvent) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText("N-ai selectat nimic");
            alert.showAndWait();
            return;
        }
        Long to = selected.getId();
        FriendRequest friendRequest = new FriendRequest(from, to, "pending");
        try {
            friendRequestService.addEntity(friendRequest);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText("Friend request successfully sent :>");
            refresh();
            alert.showAndWait();
        }catch(ValidationException |IllegalArgumentException| InputMismatchException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void refresh() {
        removeFriendRequestsFromList();
        searchTextField.clear();
        userTable.setItems(users);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTableFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        userTableLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        userTable.setItems(users);
    }

    public void searchUsers(KeyEvent keyEvent) {
        if(!searchTextField.getText().isEmpty()) {
            foundUsers.clear();
            for (User u : users)
                if(u.getFirstName().contains(searchTextField.getText()) ||
                        u.getLastName().contains(searchTextField.getText()))
                    foundUsers.add(u);
            userTable.setItems(foundUsers);
        }
    }

    public void done(ActionEvent actionEvent) {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }
}