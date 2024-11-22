package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.entity.UserEntity;
import com.name.vehicleregistration.exception.custom.user.ImageStorageException;
import com.name.vehicleregistration.exception.custom.user.InvalidImageFormatException;
import com.name.vehicleregistration.exception.custom.user.UserNotFoundException;
import com.name.vehicleregistration.model.User;
import com.name.vehicleregistration.repository.UserRepository;
import com.name.vehicleregistration.service.UserService;
import com.name.vehicleregistration.service.converters.UserConverter;
import com.name.vehicleregistration.utils.RoleUtils;
import com.name.vehicleregistration.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    final UserConverter userConverter;
    final RoleUtils roleUtils;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, RoleUtils roleUtils) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.roleUtils = roleUtils;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllProfile() {
        List<UserEntity> listUserEntities = userRepository.findAll();
        List<User> listUser = new ArrayList<>();
        for(UserEntity userEntity : listUserEntities){
            User user = userConverter.toModel(userEntity);
            listUser.add(user);
        }
        log.info("GET -> Lista de perfiles obtenida correctamente");
        return listUser;
    }

    @Override
    public User getProfile() {
        UserEntity userEntity = SecurityUtils.getAuthenticatedUser();
        if (userEntity == null) {
            throw new UserNotFoundException("No se pudo obtener datos del perfil");
        }
        log.info("GET -> Perfil personal obtenido correctamente");
        return userConverter.toModel(userEntity);
    }

    @Override
    public User getProfileById(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Perfil con ID " + id + " no encontrado."));
        log.info("GET -> Perfil obtenido correctamente");
        return userConverter.toModel(userEntity);
    }

    @Override
    public User putProfile(Integer id, User user) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Perfil con ID " + id + " no encontrado."));

        userEntity.setFullName(user.getFullName());
        userEntity.setEmail(user.getEmail());
        userEntity.setRole(roleUtils.testRole(user.getRole()));
        userEntity.setUpdatedAt(new Date().toString());

        userRepository.save(userEntity);
        log.info("PUT -> Perfil editado correctamente");
        return userConverter.toModel(userEntity);
    }

    @Override
    public User deleteProfile(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Perfil con ID " + id + " no encontrado."));
        userRepository.deleteById(id);
        log.info("DELETE -> Perfil elimminado correctamente");
        return userConverter.toModel(userEntity);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public byte[] getUserImage(Integer id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        try {
            log.info("GET -> Imagen obtenida con existo");
            return Base64.getDecoder().decode(entity.getImage());
        } catch (IllegalArgumentException e) {
            throw new InvalidImageFormatException("Formato de imagen incorrecto para el usuario con id " + id);
        }
    }

    @Override
    public void addUserImage(Integer id, MultipartFile file) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No se encontró usuario con id  " + id));
        try {
            log.info("POST -> Imagen guardada en el Usuario con id {}.", id);
            String encodedImage = Base64.getEncoder().encodeToString(file.getBytes());
            userEntity.setImage(encodedImage);
            userRepository.save(userEntity);
        } catch (IOException e) {
            throw new ImageStorageException ("No se pudo guardar la imagen del usuario con id " + id,e);
        }
    }

    @Override
    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }
}
