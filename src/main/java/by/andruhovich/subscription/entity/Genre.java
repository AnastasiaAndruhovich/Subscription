package by.andruhovich.subscription.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Entity genre
 */
public class Genre extends Entity{
    private int genreId;
    private String name;
    private String description;

    private List<Publication> publications = new LinkedList<>();

    public Genre(String name) {
        this.name = name;
    }

    public Genre(int genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }

    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Genre(int genreId, String name, String description) {
        this.genreId = genreId;
        this.name = name;
        this.description = description;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
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

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return genreId == genre.genreId &&
                Objects.equals(name, genre.name) &&
                Objects.equals(description, genre.description) &&
                Objects.equals(publications, genre.publications);
    }

    @Override
    public int hashCode() {

        return Objects.hash(genreId, name, description, publications);
    }
}
