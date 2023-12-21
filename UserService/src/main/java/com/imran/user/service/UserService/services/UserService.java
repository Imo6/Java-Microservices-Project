package com.imran.user.service.UserService.services;

import com.imran.user.service.UserService.entities.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUser();

    User getUser(String userId);
}
