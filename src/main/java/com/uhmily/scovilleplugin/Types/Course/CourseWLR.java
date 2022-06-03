package com.uhmily.scovilleplugin.Types.Course;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CourseWLR {

    private int clears;
    private int losses;

    public CourseWLR(int c, int l) {
        this.clears = c;
        this.losses = l;
    }

    public CourseWLR() {
        this(0, 0);
    }

    @JsonIgnore
    public float getWLR() {
        if (clears == 0) {
            return 0.0f;
        } else if (losses == 0) {
            return 1.0f;
        }
        return (float)clears / losses;
    }

    public int getClears() {
        return clears;
    }

    public void setClears(int clears) {
        this.clears = clears;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

}
