package by.andruhovich.subscription.entity;

import java.util.List;
import java.util.Objects;

public class PublicationType {
    private int publicationTypeId;
    private String name;

    private List<Publication> publications;

    public PublicationType(String name) {
        this.name = name;
    }

    public PublicationType(String name, List<Publication> publications) {
        this.name = name;
        this.publications = publications;
    }

    public PublicationType(int publicationTypeId, String name, List<Publication> publications) {
        this.publicationTypeId = publicationTypeId;
        this.name = name;
        this.publications = publications;
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

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationType)) return false;
        PublicationType that = (PublicationType) o;
        return publicationTypeId == that.publicationTypeId &&
                Objects.equals(name, that.name) &&
                Objects.equals(publications, that.publications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationTypeId, name, publications);
    }
}
