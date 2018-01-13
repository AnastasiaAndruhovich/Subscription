package by.andruhovich.subscription.entity;

import java.util.Date;
import java.util.Objects;


public class Block {
    private Date date;

    private User user;
    private User admin;

    public Block(Date date, User user, User admin) {
        this.date = date;
        this.user = user;
        this.admin = admin;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;
        return Objects.equals(date, block.date) &&
                Objects.equals(user, block.user) &&
                Objects.equals(admin, block.admin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date, user, admin);
    }
}
