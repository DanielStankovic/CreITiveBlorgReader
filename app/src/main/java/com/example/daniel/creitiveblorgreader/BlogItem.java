package com.example.daniel.creitiveblorgreader;






public class BlogItem {

    private int id;
    private String title;
    private String description;
    private String imageUri;


    public BlogItem(int id, String title, String description, String imageUri) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUrl) {
        this.imageUri = imageUrl;
    }


}
