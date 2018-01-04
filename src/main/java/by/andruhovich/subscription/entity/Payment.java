package by.andruhovich.subscription.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
    private int paymentNumber;
    private BigDecimal sum;
    private Date date;
    private String statement;

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

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        if (paymentNumber != payment.paymentNumber) return false;
        if (sum != null ? !sum.equals(payment.sum) : payment.sum != null) return false;
        if (date != null ? !date.equals(payment.date) : payment.date != null) return false;
        if (statement != null ? !statement.equals(payment.statement) : payment.statement != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = paymentNumber;
        result = 31 * result + (sum != null ? sum.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (statement != null ? statement.hashCode() : 0);
        return result;
    }
}
