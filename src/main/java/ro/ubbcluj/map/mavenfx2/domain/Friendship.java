package ro.ubbcluj.map.mavenfx2.domain;

import java.sql.Date;

import java.util.Objects;

public class Friendship extends Entity<Long> {

    Date date;
    Long id1;
    Long id2;

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Friendship(Long id1, Long id2, Date date) {
        this.id1 = id1;
        this.id2 = id2;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id = " + getId() +
                ", date=" + date +
                ", id1=" + id1 +
                ", id2=" + id2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(date, that.date) && Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, id1, id2);
    }
}