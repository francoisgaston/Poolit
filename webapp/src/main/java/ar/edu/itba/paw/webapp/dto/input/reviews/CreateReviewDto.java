package ar.edu.itba.paw.webapp.dto.input.reviews;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class CreateReviewDto {
    private int tripId;
    @Min(value = 1)
    @Max(value = 5)
    private int rating;

    @Size(min = 10,max = 200)
    private String comment;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
}
