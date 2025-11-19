package org.example;

import java.time.LocalDateTime;

public class Reviews {
    private int reviewID;
    private int personID;
    private int rating;
    private String comments;
    private LocalDateTime reviewDate;

    public Reviews(){}
    public Reviews(int reviewID, int personID, int rating, String comments, LocalDateTime reviewDate){
        this.reviewID = reviewID;
        this.personID = personID;
        this.rating = rating;
        this.comments = comments;
        this.reviewDate = reviewDate;
    }

    public int getReviewID(){ return reviewID; }
    public void setReviewID(int reviewID){ this.reviewID = reviewID; }
    public int getPersonID(){ return personID; }
    public void setPersonID(int personID){ this.personID = personID; }
    public int getRating(){ return rating; }
    public void setRating(int rating){ this.rating = rating; }
    public String getComments(){ return comments;}
    public void setComments(String comments){ this.comments = comments; }
    public LocalDateTime getReviewDate(){ return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate){ this.reviewDate = reviewDate; }

    @Override
    public String toString(){ return reviewID + "-" + rating; }
}
