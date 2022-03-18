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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ro.ubbcluj.map.mavenfx2.HelloApplication;
import ro.ubbcluj.map.mavenfx2.authentication.PasswordRepository;
import ro.ubbcluj.map.mavenfx2.domain.*;
import ro.ubbcluj.map.mavenfx2.domain.validators.ValidationException;
import ro.ubbcluj.map.mavenfx2.service.FriendRequestService;
import ro.ubbcluj.map.mavenfx2.service.FriendshipService;
import ro.ubbcluj.map.mavenfx2.service.MessageService;
import ro.ubbcluj.map.mavenfx2.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendController implements Initializable {
    private FriendshipService friendshipService;
    private UserService userService;
    private FriendRequestService friendRequestService;
    private MessageService messageService;
    private PasswordRepository passwordRepository;
    private Long id;
    private boolean admin = false;
    ObservableList<Friendship> friendships = FXCollections.observableArrayList();
    ObservableList<Friend> friends = FXCollections.observableArrayList();

    @FXML
    TableView<Friend> friendsTable;
    @FXML
    TableColumn<Friend, String> friendsTableFirstName;
    @FXML
    TableColumn<Friend, String> friendsTableLastName;
    @FXML
    TableColumn<Friend, String> friendsTableDate;
    @FXML
    Button addButton;
    @FXML
    Button deleteButton;
    @FXML
    Button messageButton;
    @FXML
    Button messageAllButton;
    @FXML
    Button doneButton;
    @FXML
    Button pendingButton;

    public void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
        loadFriendships();
    }

    public void setUserService(UserService userService){
        this.userService = userService;
        loadFriends();
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

    private void loadFriendships() {
        Iterable<Friendship> friendshipsIterable = friendshipService.findAll();
        List<Friendship> friendshipsList = StreamSupport.stream(friendshipsIterable.spliterator(), false)
                .collect(Collectors.toList());
        friendships.setAll(friendshipsList);
    }

    private void loadFriends() {
        Long idFriend;
        friends.clear();
        for (Friendship friendship : friendships) {
            if (Objects.equals(id, friendship.getId1()))
                idFriend = friendship.getId2();
            else if (Objects.equals(id, friendship.getId2()))
                idFriend = friendship.getId1();
            else
                continue;
            String firstName = userService.findOne(idFriend).getFirstName();
            String lastName = userService.findOne(idFriend).getLastName();
            Friend friend = new Friend(friendship.getDate().toString(), firstName, lastName, idFriend);
            friend.setId(friendship.getId());
            friends.add(friend);
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        friendsTableFirstName.setCellValueFactory(new PropertyValueFactory<Friend, String>("firstName"));
        friendsTableLastName.setCellValueFactory(new PropertyValueFactory<Friend, String>("lastName"));
        friendsTableDate.setCellValueFactory(new PropertyValueFactory<Friend, String>("date"));
        friendsTable.setItems(friends);
    }

    private void refresh() {
        friendships.clear();
        friends.clear();
        loadFriendships();
        loadFriends();
        friendsTable.setItems(friends);
    }

    public void addFriendDialog(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sendRequest.fxml"));
        Scene sendRequest = new Scene(fxmlLoader.load(), 650, 300);
        SendRequestController sendRequestController = fxmlLoader.getController();
        sendRequestController.setFrom(id);
        sendRequestController.setUserService(userService);
        sendRequestController.setFriendRequestService(friendRequestService);
        sendRequestController.setFriendshipService(friendshipService);
        window.setTitle("Send Request");
        window.setScene(sendRequest);
        window.showAndWait();

        refresh();
    }

    public void pendingRequests(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pendingRequest.fxml"));
        Scene cancelRequest = new Scene(fxmlLoader.load(), 633, 544);
        PendingRequestController cancelRequestController = fxmlLoader.getController();
        cancelRequestController.setFriendshipService(friendshipService);
        cancelRequestController.setFriendRequestService(friendRequestService);
        cancelRequestController.setUserService(userService);
        cancelRequestController.setId(id);
        window.setTitle("Pending Requests");
        window.setScene(cancelRequest);
        window.showAndWait();

        refresh();
    }

    public void deleteFriend(ActionEvent actionEvent) {
        if(!checkSelection())
            return;
        friendshipService.deleteEntity(friendsTable.getSelectionModel().getSelectedItem().getId());
        refresh();
    }



    public void messageFriend(ActionEvent actionEvent) throws IOException {
        if(!checkSelection())
            return;

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat.fxml"));
        Scene chat = new Scene(fxmlLoader.load(), 469, 491);
        ChatController chatController = fxmlLoader.getController();
        chatController.setId(id);
        chatController.setIdFriend(friendsTable.getSelectionModel().getSelectedItem().getIdFriend());
        chatController.setUserService(userService);
        chatController.setMessageService(messageService);
        window.setTitle("Chat");
        window.setScene(chat);
        window.showAndWait();
        refresh();
    }

    private boolean checkSelection() {
        if(friendsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText("N-ai selectat nimic");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void messageAllFriends(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("messageAll.fxml"));
        Scene chat = new Scene(fxmlLoader.load(), 403, 200);
        MessageAllController messageAllController = fxmlLoader.getController();
        messageAllController.setId(id);
        messageAllController.setUserService(userService);
        messageAllController.setMessageService(messageService);
        messageAllController.setFriendshipService(friendshipService);
        window.setTitle("Send a message to all your friends");
        window.setScene(chat);
        window.showAndWait();
    }

    public void setId(Long id) {
        this.id = id;
        refresh();
    }

    public void setAdmin(){
        admin = true;
        doneButton.setText("Done");
    }

    public void done(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
        if(admin)
            return;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene home = new Scene(fxmlLoader.load(), 346, 228);
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
}
