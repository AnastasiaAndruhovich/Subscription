package by.andruhovich.subscription.entity;

public class Author {
    private int authorId;
    private String publisherName;
    private String authorLastname;
    private String authorFirstname;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (authorId != author.authorId) return false;
        if (publisherName != null ? !publisherName.equals(author.publisherName) : author.publisherName != null)
            return false;
        if (authorLastname != null ? !authorLastname.equals(author.authorLastname) : author.authorLastname != null)
            return false;
        if (authorFirstname != null ? !authorFirstname.equals(author.authorFirstname) : author.authorFirstname != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = authorId;
        result = 31 * result + (publisherName != null ? publisherName.hashCode() : 0);
        result = 31 * result + (authorLastname != null ? authorLastname.hashCode() : 0);
        result = 31 * result + (authorFirstname != null ? authorFirstname.hashCode() : 0);
        return result;
    }
}
