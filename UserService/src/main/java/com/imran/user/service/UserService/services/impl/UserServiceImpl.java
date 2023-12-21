package com.imran.user.service.UserService.services.impl;

import com.imran.user.service.UserService.entities.Hotel;
import com.imran.user.service.UserService.entities.Rating;
import com.imran.user.service.UserService.entities.User;
import com.imran.user.service.UserService.exceptions.ResourceNotFoundException;
import com.imran.user.service.UserService.external.services.HotelService;
import com.imran.user.service.UserService.repositories.UserRepository;
import com.imran.user.service.UserService.services.UserService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private HotelService hotelService;

    @Autowired
    UserRepository  userRepository;
    @Autowired
    private RestTemplate restTemplate;
    private Logger logger= LoggerFactory.logger(UserServiceImpl.class);
    @Override
    public User saveUser(User user) {
        //generate unique user id
        String randomUserId= UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user= userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // fetch rating of the above  user from RATING SERVICE
        //http://localhost:8083/ratings/users/9e38175c-bf66-4aab-956e-a3c705663a24
        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.infof("{}",ratingsOfUser);
        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
        List<Rating> ratingList = ratings.stream().map(rating -> {
            /*
            ResponseEntity<Hotel> forEntity= restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel= forEntity.getBody();
             */
            Hotel hotel= hotelService.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());


        user.setRatings(ratingList);
        return user;
    }

}
