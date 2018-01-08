package by.andruhovich.subscription.entity;

import java.util.Date;
import java.util.Objects;

public class Subscription {
    private int subscriptionId;
    private int userId;
    private int publicationId;
    private Date startDate;
    private Date endDate;
    private boolean subscriptionIsActive;

    public Subscription(int userId, int publicationId, Date startDate, Date endDate, boolean subscriptionIsActive) {
        this.userId = userId;
        this.publicationId = publicationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscriptionIsActive = subscriptionIsActive;
    }

    public Subscription(int subscriptionId, int userId, int publicationId, Date startDate, Date endDate, boolean subscriptionIsActive) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.publicationId = publicationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscriptionIsActive = subscriptionIsActive;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
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

    public void setSubscriptionIsActive(boolean subscriptionIsActive) {
        this.subscriptionIsActive = subscriptionIsActive;
    }

    public boolean getSubscriptionIsActive() {
        return subscriptionIsActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return subscriptionId == that.subscriptionId &&
                userId == that.userId &&
                publicationId == that.publicationId &&
                subscriptionIsActive == that.subscriptionIsActive &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionId, userId, publicationId, startDate, endDate, subscriptionIsActive);
    }
}
