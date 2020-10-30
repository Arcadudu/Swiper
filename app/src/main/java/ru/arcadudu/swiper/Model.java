package ru.arcadudu.swiper;

import java.io.Serializable;

public class Model implements Serializable {

    private String title, content, description;
    private int image;



    // regular model
    public Model(String title, String content, int image, String description) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    // ads model
    private String link;

    public Model(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}


