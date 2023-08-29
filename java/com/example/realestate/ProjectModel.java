package com.example.realestate;

public class ProjectModel {
    private String id,headline,price,surface,rooms,location,pof,seller,discription,pin,sellerPhone;
    private String imageUrl;
    private String propertyTitle;

    // Constructor, getters, and setters


    public ProjectModel() {
    }

    public ProjectModel(String id,String headline, String price, String surface,String discription, String rooms, String location,String pin, String pof, String seller,String sellerPhone, String imageUrl) {
        this.id=id;
        this.headline = headline;
        this.price = price;
        this.surface = surface;
        this.discription = discription;
        this.rooms = rooms;
        this.location = location;
        this.pin = pin;
        this.pof = pof;
        this.seller = seller;
        this.sellerPhone = sellerPhone;
        this.imageUrl = imageUrl;

    }
    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPof() {
        return pof;
    }

    public void setPof(String pof) {
        this.pof = pof;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String seller) {
        this.sellerPhone = seller;
    }

    public String getPropertyImage() {
        return imageUrl;
    }

    public void setPropertyImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getPropertyTitle() {
        return headline;
    }
}
