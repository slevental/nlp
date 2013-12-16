package org.levental.yelp.domain;

import com.google.gson.annotations.SerializedName;
import org.joda.time.LocalDate;

/**
 * {
 * "votes": {"funny": 0, "useful": 5, "cool": 2},
 * "user_id": "rLtl8ZkDX5vH5nAx9C3q5Q",
 * "review_id": "fWKvX83p0-ka4JS3dc6E5A",
 * "stars": 5,
 * "date": "2011-01-26",
 * "text": "My wife took me here on my birthday for breakfast and it was excellent.  The weather was perfect which made sitting outside overlooking their grounds an absolute pleasure.  Our waitress was excellent and our food arrived quickly on the semi-busy Saturday morning.  It looked like the place fills up pretty quickly so the earlier you get here the better.\n\nDo yourself a favor and get their Bloody Mary.  It was phenomenal and simply the best I've ever had.  I'm pretty sure they only use ingredients from their garden and blend them fresh when you order it.  It was amazing.\n\nWhile EVERYTHING on the menu looks excellent, I had the white truffle scrambled eggs vegetable skillet and it was tasty and delicious.  It came with 2 pieces of their griddled bread with was amazing and it absolutely made the meal complete.  It was the best \"toast\" I've ever had.\n\nAnyway, I can't wait to go back!",
 * "type": "review",
 * "business_id": "9yKzy9PApeiPPOUJEtnvkg"
 * }
 */
public class Review {

    @SerializedName("user_id")
    private String userId;

    private Votes votes;
    private double stars;


    private LocalDate date;
    private String text;
    private String type;

    @SerializedName("business_id")
    private String businessId;

    public Votes getVotes() {
        return votes;
    }

    public void setVotes(Votes votes) {
        this.votes = votes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
