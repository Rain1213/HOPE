package com.example.lostandfoundoxford;

public class FoundItemModel {
    String uuid;
    String type;
    String imgUrl;
    Double latitude;
    Double longitude;
    Boolean policeProtected;

    public FoundItemModel(String uuid, String type, String imgUrl, Double latitude, Double longitude, Boolean policeProtected) {
        this.uuid = uuid;
        this.type = type;
        this.imgUrl = imgUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.policeProtected = policeProtected;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getPoliceProtected() {
        return policeProtected;
    }

    public void setPoliceProtected(Boolean policeProtected) {
        this.policeProtected = policeProtected;
    }
}
