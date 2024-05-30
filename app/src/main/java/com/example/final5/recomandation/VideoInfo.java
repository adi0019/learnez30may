package com.example.final5.recomandation;

/* loaded from: classes4.dex */
public class VideoInfo {
    private String thumbnailUrl;
    private String title;

    public VideoInfo(String title, String thumbnailUrl) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }
}