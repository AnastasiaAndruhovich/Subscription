package by.andruhovich.subscription.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Subscription {
    private int subscriptionId;
    private Date startDate;
    private Date endDate;
    private boolean subscriptionIsActive;

    private User user;
    private Publication publication;
    private List<Payment> payments;

    public Subscription(Date startDate, Date endDate, boolean subscriptionIsActive, User user, Publication publication, List<Payment> payments) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscriptionIsActive = subscriptionIsActive;
        this.user = user;
        this.publication = publication;
        this.payments = payments;
    }

    public Subscription(int subscriptionId, Date startDate, Date endDate, boolean subscriptionIsActive, User user, Publication publication, List<Payment> payments) {
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscriptionIsActive = subscriptionIsActive;
        this.user = user;
        this.publication = publication;
        this.payments = payments;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isSubscriptionIsActive() {
        return subscriptionIsActive;
    }

    public void setSubscriptionIsActive(boolean subscriptionIsActive) {
        this.subscriptionIsActive = subscriptionIsActive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return subscriptionId == that.subscriptionId &&
                subscriptionIsActive == that.subscriptionIsActive &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(user, that.user) &&
                Objects.equals(publication, that.publication) &&
                Objects.equals(payments, that.payments);
    }

    @Override
    public int hashCode() {

        return Objects.hash(subscriptionId, startDate, endDate, subscriptionIsActive, user, publication, payments);
    }
}
