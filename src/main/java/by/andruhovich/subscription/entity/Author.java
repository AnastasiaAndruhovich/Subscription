package by.andruhovich.subscription.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Author {
    private int authorId;
    private String publisherName;
    private String authorLastname;
    private String authorFirstname;

    private List<Publication> publications = new LinkedList<>();

    public Author(String publisherName, String authorLastname, String authorFirstname) {
        this.publisherName = publisherName;
        this.authorLastname = authorLastname;
        this.authorFirstname = authorFirstname;
    }

    public Author(String publisherName, String authorLastname, String authorFirstname, List<Publication> publications) {
        this.publisherName = publisherName;
        this.authorLastname = authorLastname;
        this.authorFirstname = authorFirstname;
        this.publications = publications;
    }

    public Author(int authorId, String publisherName, String authorLastname, String authorFirstname) {
        this.authorId = authorId;
        this.publisherName = publisherName;
        this.authorLastname = authorLastname;
        this.authorFirstname = authorFirstname;
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

    public String getAuthorLastname() {
        return authorLastname;
    }

    public void setAuthorLastname(String authorLastname) {
        this.authorLastname = authorLastname;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public void setAuthorFirstname(String authorFirstname) {
        this.authorFirstname = authorFirstname;
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
                Objects.equals(authorLastname, author.authorLastname) &&
                Objects.equals(authorFirstname, author.authorFirstname) &&
                Objects.equals(publications, author.publications);
    }

    @Override
    public int hashCode() {

        return Objects.hash(authorId, publisherName, authorLastname, authorFirstname, publications);
    }
}
