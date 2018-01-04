package by.andruhovich.subscription.entity;

import java.math.BigDecimal;

public class Account {
    private int accountNumber;
    private BigDecimal balance;
    private BigDecimal credit;

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

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (accountNumber != account.accountNumber) return false;
        if (balance != null ? !balance.equals(account.balance) : account.balance != null) return false;
        if (credit != null ? !credit.equals(account.credit) : account.credit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountNumber;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (credit != null ? credit.hashCode() : 0);
        return result;
    }
}
