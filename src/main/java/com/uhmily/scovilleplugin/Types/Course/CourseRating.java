package com.uhmily.scovilleplugin.Types.Course;

public class CourseRating {

    private float rating;
    private int rates;

    public CourseRating(float r, int rs) {
        this.rating = r;
        this.rates = rs;
    }

    public CourseRating() {
        this(0.0f, 0);
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRates() {
        return rates;
    }

    public void setRates(int rates) {
        this.rates = rates;
    }

    public void addRate(int rate) {
        this.rating = (this.rating*this.rates + rate)/(++this.rates);
    }

}
