package by.andruhovich.subscription.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Publication {
    private int publicationId;
    private String name;
    private int publicationTypeId;
    private int genreId;
    private String description;
    private BigDecimal price;

    public Publication(String name, int publicationTypeId, int genreId, String description, BigDecimal price) {
        this.name = name;
        this.publicationTypeId = publicationTypeId;
        this.genreId = genreId;
        this.description = description;
        this.price = price;
    }

    public Publication(int publicationId, String name, int publicationTypeId, int genreId, String description, BigDecimal price) {
        this.publicationId = publicationId;
        this.name = name;
        this.publicationTypeId = publicationTypeId;
        this.genreId = genreId;
        this.description = description;
        this.price = price;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPublicationTypeId() {
        return publicationTypeId;
    }

    public void setPublicationTypeId(int publicationTypeId) {
        this.publicationTypeId = publicationTypeId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        Publication that = (Publication) o;
        return publicationId == that.publicationId &&
                publicationTypeId == that.publicationTypeId &&
                genreId == that.genreId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId, name, publicationTypeId, genreId, description, price);
    }

    @Override
    public String toString() {
        return "Publication{" +
                "publicationId=" + publicationId +
                ", name='" + name + '\'' +
                ", publicationTypeId=" + publicationTypeId +
                ", genreId=" + genreId +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
