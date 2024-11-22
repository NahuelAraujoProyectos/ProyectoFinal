package com.name.vehicleregistration.controller;

import com.name.vehicleregistration.controller.dtos.UserRequest;
import com.name.vehicleregistration.controller.dtos.UserResponse;
import com.name.vehicleregistration.controller.mappers.UserMapper;
import com.name.vehicleregistration.model.User;
import com.name.vehicleregistration.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    final UserService userService;
    final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Llamada para obtener lista de perfiles")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<?> getAllProfile(){
        List<User> list = userService.getAllProfile();
        List<UserResponse> listResponse = new ArrayList<>();
        for(User user : list){
            UserResponse userResponse = userMapper.toResponse(user);
            listResponse.add(userResponse);
        }
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Llamada para obtener datos de perfil actual")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/get/me")
    public ResponseEntity<?> getAuthenticatedUser() {
        UserResponse userResponse = userMapper.toResponse(userService.getProfile());
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Llamada para obtener perfil por ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable Integer id){
        UserResponse userResponse = userMapper.toResponse(userService.getProfileById(id));
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Llamada para editar marca por ID")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/put/{id}")
    public ResponseEntity<?> putBrand(@PathVariable Integer id, @RequestBody UserRequest userRequest){
        UserResponse userResponse = userMapper.toResponse(userService.putProfile(id, userMapper.toModel(userRequest)));
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Llamada para eliminar perfil por ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProfile (@PathVariable Integer id){
        UserResponse userResponse = userMapper.toResponse(userService.deleteProfile(id));
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Llamada para descargar imagen")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/downloadPhoto/{id}")
    public ResponseEntity<?> downloadUserPhoto(@PathVariable("id") Integer id) {
        try {
            byte[] imageBytes = userService.getUserImage(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error downloading image for user ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Llamada para subir imagen")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @PostMapping("/userImage/{id}/add")
    public ResponseEntity<String> addUserImage(@PathVariable Integer id, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            String mimeType = imageFile.getContentType();
            if (mimeType != null && (mimeType.equals(MediaType.IMAGE_PNG_VALUE) || mimeType.equals(MediaType.IMAGE_JPEG_VALUE))) {
                userService.addUserImage(id, imageFile);
                return ResponseEntity.ok("Image successfully saved.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported file type. Only PNG and JPEG are allowed.");
        } catch (Exception e) {
            log.error("Error uploading image for user ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving image.");
        }
    }
}
