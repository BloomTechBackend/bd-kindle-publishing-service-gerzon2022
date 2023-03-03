package com.amazon.ata.kindlepublishingservice.models.response;

import java.util.Objects;

public class RemoveBookFromCatalogResponse {
    private String bookId;
    public RemoveBookFromCatalogResponse() {

    }

    public RemoveBookFromCatalogResponse(Builder builder) {
        this.bookId = builder.bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! ( o instanceof RemoveBookFromCatalogResponse )) return false;
        RemoveBookFromCatalogResponse that = (RemoveBookFromCatalogResponse) o;
        return Objects.equals(getBookId(), that.getBookId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId());
    }

    @Override
    public String toString() {
        return "RemoveBookFromCatalogResponse{" +
                "bookId='" + bookId + '\'' +
                '}';
    }

    public static final class Builder {
        private String bookId;

        public Builder() {
        }

        public Builder withBookId(String bookId) {
            this.bookId = bookId;
            return this;
        }
       public RemoveBookFromCatalogResponse build() {
            return new RemoveBookFromCatalogResponse(this);
       }
    }
}
