package by.andruhovich.subscription.entity;

import java.util.Objects;

public class PublicationType {
    private int publicationTypeId;
    private String name;

    public PublicationType(int publicationTypeId, String name) {
        this.publicationTypeId = publicationTypeId;
        this.name = name;
    }

    public PublicationType(String name) {
        this.name = name;
    }

    public int getPublicationTypeId() {
        return publicationTypeId;
    }

    public void setPublicationTypeId(int publicationTypeId) {
        this.publicationTypeId = publicationTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationType)) return false;
        PublicationType that = (PublicationType) o;
        return publicationTypeId == that.publicationTypeId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationTypeId, name);
    }

    @Override
    public String toString() {
        return "PublicationType{" +
                "publicationTypeId=" + publicationTypeId +
                ", name='" + name + '\'' +
                '}';
    }
}
