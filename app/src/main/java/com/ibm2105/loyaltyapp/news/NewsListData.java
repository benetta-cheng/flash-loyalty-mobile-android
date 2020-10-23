package com.ibm2105.loyaltyapp.news;

public class NewsListData {
    private String title;
    private int imgId;
    private String description;
    public NewsListData(String title, int imgId, String description) {
        this.title = title;
        this.imgId = imgId;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
