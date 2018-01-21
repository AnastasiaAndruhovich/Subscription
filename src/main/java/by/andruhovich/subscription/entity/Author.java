package by.andruhovich.subscription.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Author extends Entity{
    private int authorId;
    private String publisherName;
    private String authorLastName;
    private String authorFirstName;

    private List<Publication> publications = new LinkedList<>();

    public Author(String authorLastName, String authorFirstName) {
        this.authorLastName = authorLastName;
        this.authorFirstName = authorFirstName;
    }

    public Author(String publisherName, String authorLastName, String authorFirstName) {
        this.publisherName = publisherName;
        this.authorLastName = authorLastName;
        this.authorFirstName = authorFirstName;
    }

    public Author(String publisherName, String authorLastName, String authorFirstName, List<Publication> publications) {
        this.publisherName = publisherName;
        this.authorLastName = authorLastName;
        this.authorFirstName = authorFirstName;
        this.publications = publications;
    }

    public Author(int authorId, String publisherName, String authorLastName, String authorFirstName) {
        this.authorId = authorId;
        this.publisherName = publisherName;
        this.authorLastName = authorLastName;
        this.authorFirstName = authorFirstName;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
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
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return authorId == author.authorId &&
                Objects.equals(publisherName, author.publisherName) &&
                Objects.equals(authorLastName, author.authorLastName) &&
                Objects.equals(authorFirstName, author.authorFirstName) &&
                Objects.equals(publications, author.publications);
    }

    @Override
    public int hashCode() {

        return Objects.hash(authorId, publisherName, authorLastName, authorFirstName, publications);
    }
}
