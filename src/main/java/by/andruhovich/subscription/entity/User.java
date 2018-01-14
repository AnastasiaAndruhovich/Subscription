package by.andruhovich.subscription.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {
    private int userId;
    private String lastname;
    private String firstname;
    private Date birthdate;
    private String address;
    private String city;
    private int postalIndex;
    private String login;
    private String password;

    private Role role;
    private Account account;
    private List<Subscription> subscriptions = new LinkedList<>();
    private List<Payment> payments = new LinkedList<>();
    private User admin;
    private List<User> users;

    public User(String lastname, String firstname, Date birthdate, String address, String city, int postalIndex, String login, String password, Role role, Account account) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.role = role;
        this.account = account;
    }

    public User(String lastname, String firstname, Date birthdate, String address, String city, int postalIndex, String login, String password, Role role, Account account, List<Subscription> subscriptions, List<Payment> payments, User admin, List<User> users) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.role = role;
        this.account = account;
        this.subscriptions = subscriptions;
        this.payments = payments;
        this.admin = admin;
        this.users = users;
    }

    public User(int userId, String lastname, String firstname, Date birthdate, String address, String city, int postalIndex, String login, String password) {
        this.userId = userId;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
    }

    public User(int userId, String lastname, String firstname, Date birthdate, String address, String city, int postalIndex, String login, String password, Role role, Account account) {
        this.userId = userId;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.role = role;
        this.account = account;
    }

    public User(int userId, String lastname, String firstname, Date birthdate, String address, String city, int postalIndex, String login, String password, Role role, Account account, List<Subscription> subscriptions, List<Payment> payments, User admin, List<User> users) {
        this.userId = userId;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.role = role;
        this.account = account;
        this.subscriptions = subscriptions;
        this.payments = payments;
        this.admin = admin;
        this.users = users;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
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
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId &&
                postalIndex == user.postalIndex &&
                Objects.equals(lastname, user.lastname) &&
                Objects.equals(firstname, user.firstname) &&
                Objects.equals(birthdate, user.birthdate) &&
                Objects.equals(address, user.address) &&
                Objects.equals(city, user.city) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(role, user.role) &&
                Objects.equals(account, user.account) &&
                Objects.equals(subscriptions, user.subscriptions) &&
                Objects.equals(payments, user.payments) &&
                Objects.equals(admin, user.admin) &&
                Objects.equals(users, user.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, lastname, firstname, birthdate, address, city, postalIndex, login, password, role, account, subscriptions, payments, admin, users);
    }
}
