package com.appflavorsz.gceal.model;

public class GetAllData {

    String answerUrl;
    String contact;
    String description;
    String email;
    String imageUrl;
    String name;
    String pdfUrl;
    String subject;
    String title;

    public GetAllData(){}

    public GetAllData(String answerUrl, String contact0, String description, String email, String imageUrl, String name, String pdfUrl, String subject, String title) {
        this.answerUrl = answerUrl;
        this.contact = contact0;
        this.description = description;
        this.email = email;
        this.imageUrl = imageUrl;
        this.name = name;
        this.pdfUrl = pdfUrl;
        this.subject = subject;
        this.title = title;
    }

    public String getAnswerUrl() {
        return answerUrl;
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
