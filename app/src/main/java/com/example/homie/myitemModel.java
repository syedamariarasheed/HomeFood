package com.example.homie;

public class myitemModel {
    String number;
    String title;
    String category;
    String days;
    String desc_;
    String price;
    String visibility;
    String datee;
    String image;

    public myitemModel() {
    }

    public myitemModel(String number, String title, String category, String days, String desc_, String price, String visibility, String datee, String image) {
        this.number = number;
        this.title = title;
        this.category = category;
        this.days = days;
        this.desc_ = desc_;
        this.price = price;
        this.visibility = visibility;
        this.datee = datee;
        this.image = image;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDesc_() {
        return desc_;
    }

    public void setDesc_(String desc_) {
        this.desc_ = desc_;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
