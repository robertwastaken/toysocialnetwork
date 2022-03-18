package ro.ubbcluj.map.mavenfx2.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.mavenfx2.domain.Friend;
import ro.ubbcluj.map.mavenfx2.domain.Friendship;
import ro.ubbcluj.map.mavenfx2.domain.Message;
import ro.ubbcluj.map.mavenfx2.domain.validators.ValidationException;
import ro.ubbcluj.map.mavenfx2.service.FriendshipService;
import ro.ubbcluj.map.mavenfx2.service.MessageService;
import ro.ubbcluj.map.mavenfx2.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class MessageAllController {
    private UserService userService;
    private MessageService messageService;
    private FriendshipService friendshipService;
    private Long id;
    ObservableList<Message> messages = FXCollections.observableArrayList();

    @FXML
    TextField messageTextField;
    @FXML
    Button sendButton;
    @FXML
    Button cancelButton;

    public void setUserService(UserService userService){
        this.userService = userService;
    }

    public void setMessageService(MessageService messageService){
        this.messageService = messageService;
        loadMessages();
    }

    public void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }

    public void setId(Long id){
        this.id = id;
    }

    private void loadMessages() {
        Iterable<Message> messagesIterable = messageService.findAll();
        List<Message> messagesList = StreamSupport.stream(messagesIterable.spliterator(), false).toList();
        messages.setAll(messagesList);
    }

    public void sendMessage(ActionEvent actionEvent) {
        if(!messageTextField.getText().isEmpty()){
            List<Long> friendIds = getFriendsIds();
            for(Long idFriend : friendIds) {
                Message message = new Message(id, idFriend, messageTextField.getText(), LocalDateTime.now(),
                        messages.get(messages.size() - 1).getId());
                try {
                    messageService.addEntity(message);
                    messages.add(message);
                } catch (ValidationException | IllegalArgumentException | InputMismatchException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.getDialogPane().setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
            }
            alertMessageSent();
            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.close();
        }
    }

    private void alertMessageSent() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setHeaderText("Messages successfully sent :>");
        alert.showAndWait();
    }

    private List<Long> getFriendsIds() {
        Long idFriend;
        List<Long> ids = new ArrayList<Long>();
        for (Friendship friendship : friendshipService.findAll()) {
            if (Objects.equals(id, friendship.getId1()))
                ids.add(friendship.getId2());
            else if (Objects.equals(id, friendship.getId2()))
                ids.add(friendship.getId1());
        }
        return ids;
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
