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
import javafx.stage.Modality;
import javafx.stage.Stage;
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

public class PendingRequestController implements Initializable {
    private FriendshipService friendshipService;
    private UserService userService;
    private FriendRequestService friendRequestService;
    private Long id;
    ObservableList<FriendRequest> friendRequests = FXCollections.observableArrayList();
    ObservableList<FriendRequestView> inRequestsView = FXCollections.observableArrayList();
    ObservableList<FriendRequestView> outRequestsView = FXCollections.observableArrayList();

    @FXML
    TableView<FriendRequestView> inTable;
    @FXML
    TableColumn<FriendRequestView, String> inFirstName;
    @FXML
    TableColumn<FriendRequestView, String> inLastName;
    @FXML
    TableView<FriendRequestView> outTable;
    @FXML
    TableColumn<FriendRequestView, String> outFirstName;
    @FXML
    TableColumn<FriendRequestView, String> outLastName;
    @FXML
    Button inCancelButton;
    @FXML
    Button inAcceptButton;
    @FXML
    Button doneButton;
    @FXML
    Button outCancelButton;

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
        inRequestsView.clear();
        outRequestsView.clear();
        for (FriendRequest friendRequest : friendRequests) {
            if(!Objects.equals(friendRequest.getStatus(), "pending"))
                continue;
            if(Objects.equals(friendRequest.getFrom(), id)){
                String firstName = userService.findOne(friendRequest.getTo()).getFirstName();
                String lastName = userService.findOne(friendRequest.getTo()).getLastName();
                FriendRequestView friendRequestView =
                        new FriendRequestView("", "", firstName, lastName, "");
                friendRequestView.setId(friendRequest.getId());
                outRequestsView.add(friendRequestView);
            }
            else if(Objects.equals(friendRequest.getTo(), id)){
                String firstName = userService.findOne(friendRequest.getFrom()).getFirstName();
                String lastName = userService.findOne(friendRequest.getFrom()).getLastName();
                FriendRequestView friendRequestView =
                        new FriendRequestView(firstName, lastName, "", "", "");
                friendRequestView.setId(friendRequest.getId());
                inRequestsView.add(friendRequestView);
            }
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inFirstName.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("senderFirstName"));
        inLastName.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("senderLastName"));
        outFirstName.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("receiverFirstName"));
        outLastName.setCellValueFactory(new PropertyValueFactory<FriendRequestView, String>("receiverLastName"));
        inTable.setItems(inRequestsView);
        outTable.setItems(outRequestsView);
    }

    private void refresh() {
        friendRequests.clear();
        inRequestsView.clear();
        outRequestsView.clear();
        loadFriendRequests();
        loadFriendRequestsView();
        inTable.setItems(inRequestsView);
        outTable.setItems(outRequestsView);
    }

    private boolean checkSelectionIn(){
        FriendRequestView selected = inTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText("N-ai selectat nimic");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean checkSelectionOut(){
        FriendRequestView selected = outTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setHeaderText("N-ai selectat nimic");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void outCancelRequest(ActionEvent actionEvent) {
        if(!checkSelectionOut())
            return;
        FriendRequestView selected = outTable.getSelectionModel().getSelectedItem();
        friendRequestService.updateRequestStatus(selected.getId(), "rejected");
        refresh();
    }

    public void inAcceptRequest(ActionEvent actionEvent) {
        if(!checkSelectionIn())
            return;
        FriendRequestView selected = inTable.getSelectionModel().getSelectedItem();
        friendRequestService.updateRequestStatus(selected.getId(), "approved");
        FriendRequest friendRequest = new FriendRequest(null, null, null);
        for(FriendRequest f : friendRequests)
            if(Objects.equals(f.getId(), selected.getId()))
                friendRequest = f;
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        friendshipService.addEntity(new Friendship(friendRequest.getFrom(), friendRequest.getTo(), date));
        refresh();
    }

    public void inCancelRequest(ActionEvent actionEvent) {
        if(!checkSelectionIn())
            return;
        FriendRequestView selected = inTable.getSelectionModel().getSelectedItem();
        friendRequestService.updateRequestStatus(selected.getId(), "rejected");
        refresh();
    }

    public void setId(Long id) {
        this.id = id;
        refresh();
    }

    public void done(ActionEvent actionEvent) {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }
}