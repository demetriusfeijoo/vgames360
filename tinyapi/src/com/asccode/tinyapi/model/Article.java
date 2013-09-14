package com.asccode.tinyapi.model;

/**
 * @author <a href="mailto:demetrius.feijoo.91@gmail.com">Demetrius Feijoo Campos</a>
 */
public class Article {

    private int id;
    private String title;
    private int updated;
    private String link;
    private String content;

    @Override
    public String toString() {
        return getTitle();
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public int getUpdated() {
        return updated;
    }

    public String getTitle() {
        return title;
    }
}
