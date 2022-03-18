package ro.ubbcluj.map.mavenfx2.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {

    private Long idFrom;
    private Long idTo;
    private String message;
    private LocalDateTime date;
    private Long idReply;

    public Message(Long idFrom, Long idTo, String message, LocalDateTime date, Long idReply) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.message = message;
        this.date = date;
        this.idReply = idReply;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getIdReply() {
        return idReply;
    }

    public void setIdReply(Long idReply) {
        this.idReply = idReply;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id = " + getId() +
                ", from=" + getIdFrom() +
                ", to=" + getIdTo() +
                ", message='" + getMessage() + '\'' +
                ", reply='" + getIdReply() + '\'' +
                ", date=" + date +
                '}';
    }
}