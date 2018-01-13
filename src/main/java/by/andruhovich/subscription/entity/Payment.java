package by.andruhovich.subscription.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Payment {
    private int paymentNumber;
    private BigDecimal sum;
    private Date date;
    private boolean statement;

    private User user;
    private Subscription subscription;

    public Payment(BigDecimal sum, Date date, boolean statement) {
        this.sum = sum;
        this.date = date;
        this.statement = statement;
    }

    public Payment(BigDecimal sum, Date date, boolean statement, User user, Subscription subscription) {
        this.sum = sum;
        this.date = date;
        this.statement = statement;
        this.user = user;
        this.subscription = subscription;
    }

    public Payment(int paymentNumber, BigDecimal sum, Date date, boolean statement, User user, Subscription subscription) {
        this.paymentNumber = paymentNumber;
        this.sum = sum;
        this.date = date;
        this.statement = statement;
        this.user = user;
        this.subscription = subscription;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(int paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isStatement() {
        return statement;
    }

    public void setStatement(boolean statement) {
        this.statement = statement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return paymentNumber == payment.paymentNumber &&
                statement == payment.statement &&
                Objects.equals(sum, payment.sum) &&
                Objects.equals(date, payment.date) &&
                Objects.equals(user, payment.user) &&
                Objects.equals(subscription, payment.subscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentNumber, sum, date, statement, user, subscription);
    }
}
