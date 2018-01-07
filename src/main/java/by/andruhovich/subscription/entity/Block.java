package by.andruhovich.subscription.entity;

import java.util.Date;

public class Block {
    private int userId;
    private int adminId;
    private Date date;

    public Block(int userId, int adminId, Date date) {
        this.userId = userId;
        this.adminId = adminId;
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (userId != block.userId) return false;
        if (adminId != block.adminId) return false;
        if (date != null ? !date.equals(block.date) : block.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + adminId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
