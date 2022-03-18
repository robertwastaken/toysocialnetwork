package ro.ubbcluj.map.mavenfx2.domain;

import java.util.Objects;

public class FriendRequestView extends Entity<Long>{
    private String senderFirstName;
    private String senderLastName;
    private String receiverFirstName;
    private String receiverLastName;
    private String status;

    public FriendRequestView(String senderFirstName, String senderLastName, String receiverFirstName, String receiverLastName, String status) {
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.receiverFirstName = receiverFirstName;
        this.receiverLastName = receiverLastName;
        this.status = status;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getReceiverFirstName() {
        return receiverFirstName;
    }

    public void setReceiverFirstName(String receiverFirstName) {
        this.receiverFirstName = receiverFirstName;
    }

    public String getReceiverLastName() {
        return receiverLastName;
    }

    public void setReceiverLastName(String receiverLastName) {
        this.receiverLastName = receiverLastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequestView that = (FriendRequestView) o;
        return Objects.equals(senderFirstName, that.senderFirstName) && Objects.equals(senderLastName, that.senderLastName) && Objects.equals(receiverFirstName, that.receiverFirstName) && Objects.equals(receiverLastName, that.receiverLastName) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderFirstName, senderLastName, receiverFirstName, receiverLastName, status);
    }
}