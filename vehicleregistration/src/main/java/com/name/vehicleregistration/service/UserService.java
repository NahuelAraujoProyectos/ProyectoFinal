package com.name.vehicleregistration.service;

import com.name.vehicleregistration.entity.UserEntity;
import com.name.vehicleregistration.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();
    List<User> getAllProfile();
    User getProfile();
    User getProfileById(Integer id);
    User deleteProfile(Integer id);
    User putProfile(Integer id, User user);
    UserEntity save(UserEntity entity);
    byte[] getUserImage(Integer id);
    void addUserImage(Integer id, MultipartFile file);
    Optional<UserEntity> getUserById(Integer id);
}
