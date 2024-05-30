package com.example.final5;

/* loaded from: classes3.dex */
public class Contacts {
    private String name;
    private String bio;
    private String image;
    private String uid;
    private String country;
    private String nativeLanguage;
    private String city;
    private String learning;
    private String occupation;
    private String live;
  //  public boolean live;

    // Default constructor required for calls to DataSnapshot.getValue(Contacts.class)
    public Contacts() {
    }



    public Contacts(String name, String bio, String image, String uid, String country, String nativeLanguage, String city, String learning, String occupation, String live) {
        this.name = name;
        this.bio = bio;
        this.image = image;
        this.uid = uid;
        this.country = country;
        this.nativeLanguage = nativeLanguage;
        this.city = city;
        this.learning = learning;
        this.occupation = occupation;
        this.live = live;
     //   this.live=l
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLearning() {
        return learning;
    }

    public void setLearning(String learning) {
        this.learning = learning;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
