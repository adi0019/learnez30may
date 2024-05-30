package com.example.final5.recomandation;

/* loaded from: classes4.dex */
public class Video {
    private String thumbnailUrl;
    private String title;
    private String url;

    public Video(String title, String url) {
        this.title = title;
        this.url = url;
        this.thumbnailUrl = extractThumbnailUrl(url);
    }

    private String extractThumbnailUrl(String videoUrl) {
        String videoId = videoUrl.split("v=")[1];
        return "https://img.youtube.com/vi/" + videoId + "/default.jpg";
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }
}