package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.entity.UserEntity;
import com.name.vehicleregistration.exception.user.ImageStorageException;
import com.name.vehicleregistration.exception.user.InvalidImageFormatException;
import com.name.vehicleregistration.exception.user.UserNotFoundException;
import com.name.vehicleregistration.model.User;
import com.name.vehicleregistration.repository.UserRepository;
import com.name.vehicleregistration.service.UserService;
import com.name.vehicleregistration.service.converters.UserConverter;
import com.name.vehicleregistration.service.utils.RoleUtils;
import com.name.vehicleregistration.service.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RoleUtils roleUtils;

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllProfile() {
        List<UserEntity> listUserEntities = userRepository.findAll();
        List<User> listUser = new ArrayList<>();
        for(UserEntity userEntity : listUserEntities){
            User user = userConverter.toModel(userEntity);
            listUser.add(user);
        }
        log.info("GET -> Profile list successfully obtained");
        return listUser;
    }

    @Override
    public User getProfile() {
        UserEntity userEntity = SecurityUtils.getAuthenticatedUser();
        if (userEntity == null) {
            throw new UserNotFoundException("Could not get profile data");
        }
        log.info("GET -> Personal profile obtained correctly");
        return userConverter.toModel(userEntity);
    }

    @Override
    public User getProfileById(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Profile with ID " + id + " not found."));
        log.info("GET -> Profile obtained correctly");
        return userConverter.toModel(userEntity);
    }

    @Override
    public User putProfile(Integer id, User user) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Profile with ID " + id + " not found."));

        userEntity.setFullName(user.getFullName());
        userEntity.setEmail(user.getEmail());
        userEntity.setRole(roleUtils.testRole(user.getRole()));
        userEntity.setUpdatedAt(new Date().toString());

        userRepository.save(userEntity);
        log.info("PUT -> Successfully edited profile");
        return userConverter.toModel(userEntity);
    }

    @Override
    public User deleteProfile(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Profile with ID " + id + " not found."));
        userRepository.deleteById(id);
        log.info("DELETE -> Profile deleted successfully");
        return userConverter.toModel(userEntity);
    }

    @Override
    public byte[] getUserImage(Integer id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        try {
            log.info("GET -> Image obtained successfully");
            return Base64.getDecoder().decode(entity.getImage());
        } catch (IllegalArgumentException e) {
            throw new InvalidImageFormatException("Incorrect image format for user with id " + id);
        }
    }

    @Override
    public void addUserImage(Integer id, MultipartFile file) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user with id " + id + " found"));
        try {
            log.info("POST -> Image saved in User with id {}.", id);
            String encodedImage = Base64.getEncoder().encodeToString(file.getBytes());
            userEntity.setImage(encodedImage);
            userRepository.save(userEntity);
        } catch (IOException e) {
            throw new ImageStorageException ("Could not save user image with id " + id,e);
        }
    }

}
