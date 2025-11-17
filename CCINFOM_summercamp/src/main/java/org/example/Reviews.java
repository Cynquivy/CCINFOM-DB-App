package org.example;

import java.time.LocalDateTime;

public class Reviews {
    private int reviewID; 
    private int camperID; 
    private int counselorID; 
    private int rating;
    private String comments;
    private LocalDateTime reviewDate; 

    public Reviews(){} 
    public Reviews(int reviewID, int camperID, int counselorID, int rating, String comments, LocalDateTime reviewDate){
        this.reviewID = reviewID; this.camperID = camperID;  this.counselorID = counselorID; this.rating = rating; this.comments = comments; this.reviewDate = reviewDate; 
    }

    public int getReviewID(){ return reviewID; }
    public void setReviewID(int reviewID){ this.reviewID = reviewID; }
    public int getCamperID(){ return camperID; }
    public void setCamperID(int camperID){ this.camperID = camperID; }
    public int getCounselorID(){ return counselorID; } 
    public void setCounselorID(int counselorID){ this.counselorID = counselorID; }
    public int getRating(){ return rating; }
    public void setRating(int rating){ this.rating = rating; }
    public String getComments(){ return comments;}
    public void setComments(String comments){ this.comments = comments; }
    public LocalDateTime getReviewDate(){ return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate){ this.reviewDate = reviewDate; }

    @Override
    public String toString(){ return reviewID + "-" + rating; }
}
