package by.andruhovich.subscription.entity;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Publication {
    private int publicationId;
    private String name;
    private String description;
    private BigDecimal price;

    private List<Author> authors = new LinkedList<>();
    private Genre genre;
    private PublicationType publicationType;

    public Publication(String name, String description, BigDecimal price, Author author, Genre genre, PublicationType publicationType) {
        this.name = name;
        this.description = description;
        this.price = price;
        authors.add(author);
        this.genre = genre;
        this.publicationType = publicationType;
    }

    public Publication(String name, String description, BigDecimal price, List<Author> authors, Genre genre, PublicationType publicationType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.authors = authors;
        this.genre = genre;
        this.publicationType = publicationType;
    }

    public Publication(int publicationId, String name, String description, BigDecimal price) {
        this.publicationId = publicationId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Publication(int publicationId, String name, String description, BigDecimal price, List<Author> authors, Genre genre, PublicationType publicationType) {
        this.publicationId = publicationId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.authors = authors;
        this.genre = genre;
        this.publicationType = publicationType;
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

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public PublicationType getPublicationType() {
        return publicationType;
    }

    public void setPublicationType(PublicationType publicationType) {
        this.publicationType = publicationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        Publication that = (Publication) o;
        return publicationId == that.publicationId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(authors, that.authors) &&
                Objects.equals(genre, that.genre) &&
                Objects.equals(publicationType, that.publicationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId, name, description, price, authors, genre, publicationType);
    }
}
