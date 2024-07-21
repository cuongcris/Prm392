package com.example.bluegreen.Domain;

public class Comment {
    private String id;
    private String userEmail;
    private String productId;
    private String content;
    private String timestamp;

    // Constructor
    public Comment(String id, String userEmail, String productId, String content, String timestamp) {
        this.id = id;
        this.userEmail = userEmail;
        this.productId = productId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Comment(){

    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userId) {
        this.userEmail = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

