package by.andruhovich.subscription.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Payment {
    private int paymentNumber;
    private int userId;
    private int subscriptionId;
    private BigDecimal sum;
    private Date date;
    private String statement;

    public Payment(int userId, int subscriptionId, BigDecimal sum, Date date, String statement) {
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.sum = sum;
        this.date = date;
        this.statement = statement;
    }

    public Payment(int paymentNumber, int userId, int subscriptionId, BigDecimal sum, Date date, String statement) {
        this.paymentNumber = paymentNumber;
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.sum = sum;
        this.date = date;
        this.statement = statement;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(int paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
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

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return paymentNumber == payment.paymentNumber &&
                userId == payment.userId &&
                subscriptionId == payment.subscriptionId &&
                Objects.equals(sum, payment.sum) &&
                Objects.equals(date, payment.date) &&
                Objects.equals(statement, payment.statement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentNumber, userId, subscriptionId, sum, date, statement);
    }
}
