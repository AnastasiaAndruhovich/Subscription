package by.andruhovich.subscription.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private int accountNumber;
    private BigDecimal balance;
    private BigDecimal loan;

    public Account(BigDecimal balance, BigDecimal loan) {
        this.balance = balance;
        this.loan = loan;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return accountNumber == account.accountNumber &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(loan, account.loan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, balance, loan);
    }
}
