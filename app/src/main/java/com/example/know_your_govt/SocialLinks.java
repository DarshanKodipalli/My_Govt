package com.example.know_your_govt;

import java.io.Serializable;

public class SocialLinks implements Serializable{
    private String googlePlusIdentifier;
    private String facebookIdentifier;
    private String twitterIdentifier;
    private String youtubeIdentifier;

    public SocialLinks(String g,String f,String t,String y){
        this.googlePlusIdentifier = g;
        this.facebookIdentifier = f;
        this.twitterIdentifier = t;
        this.youtubeIdentifier = y;
    }
    public String getGooglePlusIdentifier() {
        return googlePlusIdentifier;
    }

    public void setGooglePlusIdentifier(String googlePlusIdentifier) {
            this.googlePlusIdentifier = googlePlusIdentifier;
    }

    public String getFacebookIdentifier() {

        return facebookIdentifier;
    }

    public void setFacebookIdentifier(String facebookIdentifier) {
        this.facebookIdentifier = facebookIdentifier;
    }

    public String getTwitterIdentifier() {
        return twitterIdentifier;
    }

    public void setTwitterIdentifier(String twitterIdentifier) {
        this.twitterIdentifier = twitterIdentifier;
    }

    public String getYoutubeIdentifier() {
        return youtubeIdentifier;
    }

    public void setYoutubeIdentifier(String youtubeIdentifier) {
        this.youtubeIdentifier = youtubeIdentifier;
    }
}
