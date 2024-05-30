package com.example.final5;

public class videomodel
{
  String bio,Title,url;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public videomodel(String bio, String Title, String url) {
        this.bio= bio;
        this.Title = Title;
        this.url = url;
    }
    videomodel()
    {

    }


}
