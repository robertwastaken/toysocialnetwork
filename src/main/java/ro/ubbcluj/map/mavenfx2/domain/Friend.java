package ro.ubbcluj.map.mavenfx2.domain;

import java.sql.Date;
import java.util.Objects;

public class Friend extends Entity<Long> {

    String date;
    String firstName;
    String lastName;
    Long idFriend;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getIdFriend() {
        return idFriend;
    }

    public void setIdFriend(Long idFriend) {
        this.idFriend = idFriend;
    }

    public Friend(String date, String firstName, String lastName, Long idFriend) {
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idFriend = idFriend;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "date='" + date + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", idFriend=" + idFriend +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return Objects.equals(date, friend.date) && Objects.equals(firstName, friend.firstName) && Objects.equals(lastName, friend.lastName) && Objects.equals(idFriend, friend.idFriend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, firstName, lastName, idFriend);
    }
}