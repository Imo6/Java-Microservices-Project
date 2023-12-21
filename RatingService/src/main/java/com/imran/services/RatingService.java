package com.imran.services;

import com.imran.entities.Rating;

import java.util.List;

public interface RatingService {

    public Rating create(Rating rating);
    public List<Rating> getRatings();
    public List<Rating> getRatingByUserId(String userId);
    public List<Rating> getRatingByHotelId(String hotelId);
}
