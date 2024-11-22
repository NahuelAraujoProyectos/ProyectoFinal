package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.entity.ProfileEntity;
import com.name.vehicleregistration.entity.UserEntity;
import com.name.vehicleregistration.exception.custom.user.UserNotFoundException;
import com.name.vehicleregistration.model.User;
import com.name.vehicleregistration.repository.UserRepository;
import com.name.vehicleregistration.service.converters.UserConverter;
import com.name.vehicleregistration.utils.RoleUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private RoleUtils roleUtils;

    @Test
    void GetAllProfile_test() {
        // Given
        UserEntity userEntity1 = new UserEntity();
        UserEntity userEntity2 = new UserEntity();
        List<UserEntity> userEntities = Arrays.asList(userEntity1, userEntity2);
        User user1 = new User();
        User user2 = new User();

        // When
        when(userRepository.findAll()).thenReturn(userEntities);
        when(userConverter.toModel(userEntity1)).thenReturn(user1);
        when(userConverter.toModel(userEntity2)).thenReturn(user2);
        List<User> users = userService.getAllProfile();

        // Then
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void getProfileById_test(){
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setFullName("FullName");
        userEntity.setEmail("email@email.com");
        userEntity.setRole("Role");

        User user = new User();
        user.setId(1);
        user.setFullName("FullName");
        user.setEmail("email@email.com");
        user.setRole("Role");

        // When
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(userConverter.toModel(userEntity)).thenReturn(user);
        User result = userService.getProfileById(1);

        // Then
        assertEquals("FullName", result.getFullName());
        assertEquals("email@email.com", result.getEmail());
        assertEquals("Role", result.getRole());
    }

    @Test
    void getProfileById_ko() {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // When & Then: Verificar que se lanza la excepción esperada
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getProfileById(1);
        });
    }

    @Test
    void putProfile_test() {
        // Given
        Integer id = 1;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setFullName("Old Name");
        userEntity.setEmail("old.email@example.com");
        userEntity.setRole("USER");

        User user = new User();
        user.setId(id);
        user.setFullName("New Name");
        user.setEmail("new.email@example.com");
        user.setRole("ADMIN");

        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setFullName("New Name");
        updatedUser.setEmail("new.email@example.com");
        updatedUser.setRole("ADMIN");

        // When
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(roleUtils.testRole(user.getRole())).thenReturn("ADMIN");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userConverter.toModel(userEntity)).thenReturn(updatedUser);
        User result = userService.putProfile(id, user);

        // Then
        assertNotNull(result);
        assertEquals("New Name", result.getFullName());
        assertEquals("new.email@example.com", result.getEmail());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void putProfile_ko() {
        // Given
        Integer id = 1;
        User user = new User();
        user.setId(id);
        user.setFullName("New Name");
        user.setEmail("new.email@example.com");
        user.setRole("ADMIN");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.putProfile(id, user);
        });
    }

    @Test
    void deleteProfile_test() {
        // Given
        Integer id = 1;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setFullName("Name to Delete");
        userEntity.setEmail("delete.email@example.com");
        userEntity.setRole("USER");

        User user = new User();
        user.setId(id);
        user.setFullName("Name to Delete");
        user.setEmail("delete.email@example.com");
        user.setRole("USER");

        // When
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userConverter.toModel(userEntity)).thenReturn(user);
        User result = userService.deleteProfile(id);

        // Then
        assertNotNull(result);
        assertEquals("Name to Delete", result.getFullName());
    }

    @Test
    void deleteProfile_ko() {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteProfile(1);
        });
    }


}
