package com.example.homie;

public class Ordermodel {
    String sellerNumber;
    String title;
    String category;
    String days;
    String desc_;
    String price;
    String Quan;
    String datee;
    String image;
    String Loc;
    String orderreqnumber;

    public Ordermodel(String sellerNumber, String title, String category, String days, String desc_, String price, String quan, String datee, String image, String loc, String orderreqnumber) {
        this.sellerNumber = sellerNumber;
        this.title = title;
        this.category = category;
        this.days = days;
        this.desc_ = desc_;
        this.price = price;
        Quan = quan;
        this.datee = datee;
        this.image = image;
        Loc = loc;
        this.orderreqnumber = orderreqnumber;
    }

    public String getSellerNumber() {
        return sellerNumber;
    }

    public void setSellerNumber(String sellerNumber) {
        this.sellerNumber = sellerNumber;
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

    public String getQuan() {
        return Quan;
    }

    public void setQuan(String quan) {
        Quan = quan;
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

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public String getOrderreqnumber() {
        return orderreqnumber;
    }

    public void setOrderreqnumber(String orderreqnumber) {
        this.orderreqnumber = orderreqnumber;
    }

    public Ordermodel() {
    }
}
