package ro.ubbcluj.map.mavenfx2.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import ro.ubbcluj.map.mavenfx2.domain.Message;
import ro.ubbcluj.map.mavenfx2.domain.User;
import ro.ubbcluj.map.mavenfx2.domain.validators.ValidationException;
import ro.ubbcluj.map.mavenfx2.service.MessageService;
import ro.ubbcluj.map.mavenfx2.service.UserService;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class ChatController implements Initializable {

    private UserService userService;
    private MessageService messageService;
    private Long id;
    private Long idFriend;
    ObservableList<Message> messages = FXCollections.observableArrayList();


    @FXML
    VBox chatVBox;
    @FXML
    TextField textField;
    @FXML
    Button sendButton;
    @FXML
    Text friendNameText;

    public void setUserService(UserService userService){
        this.userService = userService;
        setFriendNameText();
    }

    public void setMessageService(MessageService messageService){
        this.messageService = messageService;
        loadMessages();
        showMessages();
    }

    private void loadMessages() {
        Iterable<Message> messagesIterable = messageService.findAll();
        List<Message> messagesList = StreamSupport.stream(messagesIterable.spliterator(), false).toList();
        messages.setAll(messagesList);
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setIdFriend(Long idFriend){
        this.idFriend = idFriend;
    }

    public void sendMessage(ActionEvent actionEvent) {
        if(!textField.getText().isEmpty()){
            Message message = new Message(id, idFriend, textField.getText(), LocalDateTime.now(),
                    messages.get(messages.size() - 1).getId());
            textField.clear();
            try {
                messageService.addEntity(message);
                messages.add(message);
                createMessageBubble(message);
            }catch(ValidationException |IllegalArgumentException| InputMismatchException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.getDialogPane().setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void showMessages() {
        for(Message m : messages){
            if(!((Objects.equals(m.getIdFrom(), id) && Objects.equals(m.getIdTo(), idFriend)) ||
                    (Objects.equals(m.getIdFrom(), idFriend) && Objects.equals(m.getIdTo(), id))))
                continue;
            createMessageBubble(m);
        }
    }

    private void createMessageBubble(Message m) {
        HBox hbox = new HBox();
        Text text = new Text(m.getMessage());
        text.setFill(Color.WHITE);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 10, 10));
        if(Objects.equals(m.getIdFrom(), id)) {
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setPadding(new Insets(5, 5, 10, 10));
            textFlow.setStyle("-fx-background-color: rgb(15, 125, 242);" +
                    "-fx-background-radius: 15px;");

        }
        else {
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(5, 10, 5, 5));
            textFlow.setStyle("-fx-background-color: rgb(153, 102, 255);" +
                    "-fx-background-radius: 15px;");
        }
        hbox.getChildren().add(textFlow);
        chatVBox.getChildren().add(hbox);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void setFriendNameText() {
        User user = userService.findOne(idFriend);
        friendNameText.setText(user.getFirstName() + " " + user.getLastName());
    }
}
