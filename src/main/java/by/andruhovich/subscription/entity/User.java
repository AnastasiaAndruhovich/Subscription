package by.andruhovich.subscription.entity;

import java.sql.Date;
import java.util.Objects;

public class User {
    private int userId;
    private int roleId;
    private String lastname;
    private String firstname;
    private Date birthdate;
    private String address;
    private String city;
    private int postalIndex;
    private int accountNumber;
    private String login;
    private String password;

    public User(int roleId, String lastname, String firstname, Date birthdate, String address, String city,
                int postalIndex, int accountNumber, String login, String password) {
        this.roleId = roleId;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.accountNumber = accountNumber;
        this.login = login;
        this.password = password;
    }

    public User(int userId, int roleId, String lastname, String firstname, Date birthdate, String address, String city,
                int postalIndex, int accountNumber, String login, String password) {
        this.userId = userId;
        this.roleId = roleId;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.accountNumber = accountNumber;
        this.login = login;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalIndex() {
        return postalIndex;
    }

    public void setPostalIndex(int postalIndex) {
        this.postalIndex = postalIndex;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId &&
                roleId == user.roleId &&
                postalIndex == user.postalIndex &&
                accountNumber == user.accountNumber &&
                Objects.equals(lastname, user.lastname) &&
                Objects.equals(firstname, user.firstname) &&
                Objects.equals(birthdate, user.birthdate) &&
                Objects.equals(address, user.address) &&
                Objects.equals(city, user.city) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId, lastname, firstname, birthdate, address, city, postalIndex, accountNumber, login, password);
    }
}
