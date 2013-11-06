package com.asccode.tinyapi.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:demetrius.feijoo.91@gmail.com">Demetrius Feijoo Campos</a>
 */
// TODO: Extrair dados de feed para uma classe feed!!!!
public class Article implements Serializable{

    private int id;
    private String author;
    private String title;
    private String feed_title;
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

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getFeedTitle() {
        return feed_title;
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
}
