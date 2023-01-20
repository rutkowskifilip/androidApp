package com.example.rutkowski001;

public class Note {
    private String id;
    private String title;
    private String description;
    private String color;
    private String imagePath;
    public Note(String id, String title, String description, String color, String imagePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.color = color;
        this.imagePath = imagePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}
