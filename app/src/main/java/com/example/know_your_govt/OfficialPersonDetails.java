package com.example.know_your_govt;

import java.io.Serializable;

public class OfficialPersonDetails implements Serializable{

    private String officeName;
    private String personName;
    private String address;
    private String partyName;
    private String phone;
    private String url;
    private String emailId;
    private String photoLink;
    private SocialLinks socialLinks;

    public OfficialPersonDetails() {


    }

    public OfficialPersonDetails(String personName, String officeName, String partyName, String address, String phone, String url, String emailId, String photoLink, String googlePlusIdentifier, String facebookIdentifier, String twitterIdentifier, String youtubeIdentifier) {
        this.personName= personName;
        this.partyName = partyName;
        this.phone = phone;
        this.officeName= officeName;
        this.address= address;
        this.photoLink= photoLink;
        this.emailId= emailId;
        this.url= url;
        this.socialLinks = new SocialLinks(googlePlusIdentifier,facebookIdentifier,twitterIdentifier,youtubeIdentifier);
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public SocialLinks getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(SocialLinks socialLinks) {
        this.socialLinks = socialLinks;
    }
}
