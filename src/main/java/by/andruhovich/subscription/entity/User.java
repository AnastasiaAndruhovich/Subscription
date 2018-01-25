package by.andruhovich.subscription.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User extends Entity{
    private int userId;
    private String lastName;
    private String firstName;
    private Date birthDate;
    private String address;
    private String city;
    private int postalIndex;
    private String login;
    private String password;

    private Role role;
    private Account account;
    private List<Subscription> subscriptions = new LinkedList<>();
    private User admin;
    private List<User> users;

    public User(String lastName, String firstName, Date birthDate, String address, String city, int postalIndex,
                String login, String password, Account account) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.account = account;
    }

    public User(String lastName, String firstName, Date birthDate, String address, String city, int postalIndex,
                String login, String password, Role role, Account account) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.role = role;
        this.account = account;
    }

    public User(String lastName, String firstName, Date birthDate, String address, String city, int postalIndex,
                String login, String password, Role role, Account account, List<Subscription> subscriptions,
                User admin, List<User> users) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.role = role;
        this.account = account;
        this.subscriptions = subscriptions;
        this.admin = admin;
        this.users = users;
    }

    public User(int userId, String lastName, String firstName, Date birthDate, String address, String city, int postalIndex, String login, String password) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
    }

    public User(int userId, String lastName, String firstName, Date birthDate, String address, String city, int postalIndex, String login, String password, Role role, Account account) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.role = role;
        this.account = account;
    }

    public User(int userId, String lastName, String firstName, Date birthDate, String address, String city,
                int postalIndex, String login, String password, Role role, Account account,
                List<Subscription> subscriptions, User admin, List<User> users) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.city = city;
        this.postalIndex = postalIndex;
        this.login = login;
        this.password = password;
        this.role = role;
        this.account = account;
        this.subscriptions = subscriptions;
        this.admin = admin;
        this.users = users;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(birthDate, user.birthDate) &&
                Objects.equals(address, user.address) &&
                Objects.equals(city, user.city) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(role, user.role) &&
                Objects.equals(account, user.account) &&
                Objects.equals(subscriptions, user.subscriptions) &&
                Objects.equals(admin, user.admin) &&
                Objects.equals(users, user.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, lastName, firstName, birthDate, address, city, postalIndex, login, password, role,
                account, subscriptions, admin, users);
    }
}
