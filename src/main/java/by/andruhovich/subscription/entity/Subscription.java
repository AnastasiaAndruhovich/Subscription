package by.andruhovich.subscription.entity;

import java.util.Date;
import java.util.Objects;

public class Subscription {
    private int subscriptionId;
    private int userId;
    private int publicationId;
    private Date startData;
    private Date endData;
    private boolean subscriptionIsActive;

    public Subscription(int userId, int publicationId, Date startData, Date endData, boolean subscriptionIsActive) {
        this.userId = userId;
        this.publicationId = publicationId;
        this.startData = startData;
        this.endData = endData;
        this.subscriptionIsActive = subscriptionIsActive;
    }

    public Subscription(int subscriptionId, int userId, int publicationId, Date startData, Date endData, boolean subscriptionIsActive) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.publicationId = publicationId;
        this.startData = startData;
        this.endData = endData;
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

    public Date getStartData() {
        return startData;
    }

    public void setStartData(Date startData) {
        this.startData = startData;
    }

    public Date getEndData() {
        return endData;
    }

    public void setEndData(Date endData) {
        this.endData = endData;
    }

    public boolean isSubscriptionIsActive() {
        return subscriptionIsActive;
    }

    public void setSubscriptionIsActive(boolean subscriptionIsActive) {
        this.subscriptionIsActive = subscriptionIsActive;
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
                Objects.equals(startData, that.startData) &&
                Objects.equals(endData, that.endData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionId, userId, publicationId, startData, endData, subscriptionIsActive);
    }
}
