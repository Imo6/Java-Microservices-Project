package com.imran.user.service.UserService.controllers;

import com.imran.user.service.UserService.entities.User;
import com.imran.user.service.UserService.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1= userService.saveUser(user);
        return  ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

//    int retryCount=1;

    @GetMapping("/{userId}")
//    @CircuitBreaker(name= "ratingHotelBreaker", fallbackMethod = "ratingFallbackMethod")
//    @Retry(name="ratingHotelService",fallbackMethod = "ratingFallbackMethod")
    @RateLimiter(name="userRateLimiter", fallbackMethod = "ratingFallbackMethod")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        logger.info("Get Single User Handler: UserController");
//        logger.info("Retry Count {}",retryCount);
//        retryCount++;
        User singleuser= userService.getUser(userId);
        return ResponseEntity.ok(singleuser);
    }

    public ResponseEntity<User> ratingFallbackMethod(String userId, Exception ex){
        ex.printStackTrace();
        logger.info("Fallback is executed because service is down : ", ex.getMessage());
        User user= User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .About("This user is created because service is down")
                .userId("1234")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> alluser= userService.getAllUser();
        return  ResponseEntity.ok(alluser);
    }
}
