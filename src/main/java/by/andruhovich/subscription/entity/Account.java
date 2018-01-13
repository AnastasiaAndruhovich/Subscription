package by.andruhovich.subscription.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private int accountNumber;
    private BigDecimal balance;
    private BigDecimal loan;

    private User user;

    public Account(BigDecimal balance, BigDecimal loan) {
        this.balance = balance;
        this.loan = loan;
    }

    public Account(BigDecimal balance, BigDecimal loan, User user) {
        this.balance = balance;
        this.loan = loan;
        this.user = user;
    }

    public Account(int accountNumber, BigDecimal balance, BigDecimal loan) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.loan = loan;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getLoan() {
        return loan;
    }

    public void setLoan(BigDecimal loan) {
        this.loan = loan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return accountNumber == account.accountNumber &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(loan, account.loan) &&
                Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(accountNumber, balance, loan, user);
    }
}
