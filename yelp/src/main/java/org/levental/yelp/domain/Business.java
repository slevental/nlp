package org.levental.yelp.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * {
 * "business_id": "rncjoVoEFUJGCUoC1JgnUA",
 * "full_address": "8466 W Peoria Ave\nSte 6\nPeoria, AZ 85345",
 * "open": true,
 * "categories": ["Accountants", "Professional Services", "Tax Services", "Financial Services"],
 * "city": "Peoria",
 * "review_count": 3,
 * "name": "Peoria Income Tax Service",
 * "neighborhoods": [],
 * "longitude": -112.241596,
 * "state": "AZ",
 * "stars": 5.0,
 * "latitude": 33.581867000000003,
 * "type": "business"
 * }
 */
public class Business {
    @SerializedName("business_id")
    private String businessId;

    @SerializedName("full_address")
    private String fullAddress;

    private boolean open;
    private List<String> categories;
    private String city;

    @SerializedName("review_count")
    private int reviewCount;
    private String name;
    private List<String> neighborhoods;
    private double longitude;
    private double latitude;
    private String state;
    private double stars;
    private String type;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNeighborhoods() {
        return neighborhoods;
    }

    public void setNeighborhoods(List<String> neighborhoods) {
        this.neighborhoods = neighborhoods;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
