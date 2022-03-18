package ro.ubbcluj.map.mavenfx2.domain;

import java.util.Objects;

public class FriendRequest extends Entity<Long>{
    private Long from;
    private Long to;
    private String status;

    public FriendRequest(Long from, Long to, String status) {
        this.from = from;
        this.to = to;
        this.status = status;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
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
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, status);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + getId() +
                ", id_from=" + from +
                ", id_to=" + to +
                ", status='" + status + '\'' +
                '}';
    }
}