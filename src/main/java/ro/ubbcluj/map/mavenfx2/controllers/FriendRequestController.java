package ro.ubbcluj.map.mavenfx2.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.mavenfx2.domain.Friend;
import ro.ubbcluj.map.mavenfx2.domain.FriendRequest;
import ro.ubbcluj.map.mavenfx2.domain.FriendRequestView;
import ro.ubbcluj.map.mavenfx2.domain.Friendship;
import ro.ubbcluj.map.mavenfx2.service.FriendRequestService;
import ro.ubbcluj.map.mavenfx2.service.FriendshipService;
import ro.ubbcluj.map.mavenfx2.service.UserService;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestController implements Initializable {
    private FriendshipService friendshipService;
    private UserService userService;
    private FriendRequestService friendRequestService;
    ObservableList<FriendRequest> friendRequests = FXCollections.observableArrayList();
    ObservableList<FriendRequestView> friendRequestsView = FXCollections.observableArrayList();

    @FXML
    TableView<FriendRequestView> friendRequestsTable;
    @FXML
    TableColumn<FriendRequestView, Long> requestId;
    @FXML
    TableColumn<FriendRequestView, String> senderFirstName;
    @FXML
    TableColumn<FriendRequestView, String> senderLastName;
    @FXML
    TableColumn<FriendRequestView, String> receiverFirstName;
    @FXML
    TableColumn<FriendRequestView, String> receiverLastName;
    @FXML
    TableColumn<FriendRequestView, String> status;
    @FXML
    Button approveButton;
    @FXML
    Button declineButton;
    @FXML
    Button doneButton;

    public void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }

    public void setUserService(UserService userService){
        this.userService = userService;
        loadFriendRequestsView();
    }

    public void setFriendRequestService(FriendRequestService friendRequestService){
        this.friendRequestService = friendRequestService;
        loadFriendRequests();
    }

    public void loadFriendRequests(){
        Iterable<FriendRequest> friendRequestsIterable = friendRequestService.findAll();
        List<FriendRequest> friendRequestsList = StreamSupport
                .stream(friendRequestsIterable.spliterator(), false)
                .collect(Collectors.toList());
        friendRequests.setAll(friendRequestsList);
    }

    private void loadFriendRequestsView() {
        friendRequestsView.clear();
        for (FriendRequest friendRequest : friendRequests) {
            String senderFirstName = userService.findOne(friendRequest.getFrom()).getFirstName();
            String senderLastName = userService.findOne(friendRequest.getFrom()).getLastName();
            String receiverFirstName = userService.findOne(friendRequest.getTo()).getFirstName();
            String receiverLastName = userService.findOne(friendRequest.getTo()).getLastName();
            String status = friendRequest.getStatus();
            FriendRequestView friendRequestView =
                    new FriendRequestView(senderFirstName, senderLastName,
                            receiverFirstName, receiverLastName, status);
            friendRequestView.setId(friendRequest.getId());
            friendRequestsView.add(friendRequestView);
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestId.setCellValueFactory(new PropertyValueFactory<FriendRequestView, Long>("id"));
        senderFirstName.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("senderFirstName"));
        senderLastName.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("senderLastName"));
        receiverFirstName.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("receiverFirstName"));
        receiverLastName.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("receiverLastName"));
        status.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("status"));
        friendRequestsTable.setItems(friendRequestsView);
    }

    private void refresh() {
        friendRequests.clear();
        friendRequestsView.clear();
        loadFriendRequests();
        loadFriendRequestsView();
        friendRequestsTable.setItems(friendRequestsView);
    }

    private boolean checkSelection(){
        FriendRequestView selected = friendRequestsTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText("N-ai selectat nimic");
            alert.showAndWait();
            return false;
        }
        if(!Objects.equals(selected.getStatus(), "pending")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText("Cererea a fost deja rezolvata.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void approveRequest(ActionEvent actionEvent) {
        if(!checkSelection())
            return;
        FriendRequestView selected = friendRequestsTable.getSelectionModel().getSelectedItem();
        friendRequestService.updateRequestStatus(selected.getId(), "approved");
        FriendRequest friendRequest = new FriendRequest(null, null, null);
        for(FriendRequest f : friendRequests)
            if(Objects.equals(f.getId(), selected.getId()))
                friendRequest = f;
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        friendshipService.addEntity(new Friendship(friendRequest.getFrom(), friendRequest.getTo(), date));
        refresh();
    }

    public void declineRequest(ActionEvent actionEvent) {
        if(!checkSelection())
            return;
        FriendRequestView selected = friendRequestsTable.getSelectionModel().getSelectedItem();
        friendRequestService.updateRequestStatus(selected.getId(), "rejected");
        refresh();
    }

    public void done(ActionEvent actionEvent) {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }
}
