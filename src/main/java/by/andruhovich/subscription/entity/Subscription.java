package by.andruhovich.subscription.entity;

import java.util.Date;

public class Subscription {
    private int subscriptionId;
    private Date startData;
    private Date endData;
    private String subscriptionIsActive;

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
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

    public String getSubscriptionIsActive() {
        return subscriptionIsActive;
    }

    public void setSubscriptionIsActive(String subscriptionIsActive) {
        this.subscriptionIsActive = subscriptionIsActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (subscriptionId != that.subscriptionId) return false;
        if (startData != null ? !startData.equals(that.startData) : that.startData != null) return false;
        if (endData != null ? !endData.equals(that.endData) : that.endData != null) return false;
        if (subscriptionIsActive != null ? !subscriptionIsActive.equals(that.subscriptionIsActive) : that.subscriptionIsActive != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subscriptionId;
        result = 31 * result + (startData != null ? startData.hashCode() : 0);
        result = 31 * result + (endData != null ? endData.hashCode() : 0);
        result = 31 * result + (subscriptionIsActive != null ? subscriptionIsActive.hashCode() : 0);
        return result;
    }
}
