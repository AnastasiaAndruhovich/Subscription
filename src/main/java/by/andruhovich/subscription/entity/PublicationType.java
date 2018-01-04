package by.andruhovich.subscription.entity;

public enum PublicationType {
    NEWSPAPER, MAGAZINE, BOOK;
    private int publicationTypeId;

    public void setPublicationTypeId(int publicationTypeId) {
        this.publicationTypeId = publicationTypeId;
    }

    public int getPublicationTypeId() {
        return publicationTypeId;
    }
}
