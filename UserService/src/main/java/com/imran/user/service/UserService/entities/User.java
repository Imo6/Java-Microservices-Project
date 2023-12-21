package com.imran.user.service.UserService.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="micro_user")
@Builder
public class User {

    @Id
    @Column(name="user_id")
    private String userId;
    @Column(name="NAME")
    private  String name;
    @Column(name="EMAIL")
    private  String email;
    @Column(name="ABOUT")
    private String About;

    @Transient
    private List<Rating> ratings= new ArrayList<>();


    public User(){
        super();
    }

    public User(String userId, String name, String email, String about, List<Rating> ratings) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        About = about;
        this.ratings = ratings;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
