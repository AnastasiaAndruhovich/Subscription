package by.andruhovich.subscription.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Role {
    private int roleId;
    private String name;

    private List<User> users = new LinkedList<>();

    public Role(String name) {
        this.name = name;
    }

    public Role(int roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public Role(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }

    public Role(int roleId, String name, List<User> users) {
        this.roleId = roleId;
        this.name = name;
        this.users = users;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return roleId == role.roleId &&
                Objects.equals(name, role.name) &&
                Objects.equals(users, role.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, name, users);
    }
}
