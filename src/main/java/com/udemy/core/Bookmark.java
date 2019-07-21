package com.udemy.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bookmarks")
@NamedQueries({
        @NamedQuery(name = "com.udemy.core.Bookmark.findForUser",
                query = "select b from Bookmark b where b.user.id = :id"),
        @NamedQuery(name = "com.udemy.core.Bookmark.remove",
                query = "delete from Bookmark b where b.id = :id"),
})
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String url;
    private String description;
    @JsonIgnore
    @ManyToOne
    private User user;

    public Bookmark() {
    }

    public Bookmark(String name, String url, String description, User owner) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.user = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return id == bookmark.id &&
                Objects.equals(name, bookmark.name) &&
                Objects.equals(url, bookmark.url) &&
                Objects.equals(description, bookmark.description) &&
                Objects.equals(user, bookmark.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, description, user);
    }
}
