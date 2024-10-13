package com.example.together.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.together.dto.request.UpdatePasswordRequest;
import com.example.together.dto.request.UserCreationRequest;
import com.example.together.dto.request.UserUpdateRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.UserResponse;
import com.example.together.model.User;
import com.example.together.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;


    private static int imageCount = 1;
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }

    @PutMapping("/password/{userId}")
    ApiResponse<UserResponse> updatePassword(
            @PathVariable String userId,
            @RequestBody @Valid UpdatePasswordRequest request) {

        UserResponse updatedUser = userService.updatePassword(userId, request);
        return ApiResponse.<UserResponse>builder()
                .result(updatedUser)
                .build();
    }

    
    @PostMapping("/{id}/upload-avatar")
    public ApiResponse<String> uploadAvatar(@PathVariable String id, @RequestParam("image") MultipartFile image) {
        try{
            String oldfileName = image.getOriginalFilename();
            int dotIndex = oldfileName.lastIndexOf('.');
            String extension = oldfileName.substring(dotIndex);
            String fileName = String.valueOf(imageCount) + extension;
            imageCount++;
            String imageDirectory = "/root/project/nginx/images";
            Path filePath = Paths.get(imageDirectory, fileName);
            Files.write(filePath, image.getBytes());
            String fileSaved = "http://14.225.254.35/images/" + fileName;
            int savedImage = userService.saveAvatarImage(id,fileSaved);
            if(savedImage == 1){
                return ApiResponse.<String>builder()
                        .result("Upload image successfully")
                        .build();
            }
            else {
                return ApiResponse.<String>builder()
                        .result("Upload image not successfully")
                        .build();
            }
        } catch (IOException e) {
            System.out.println(e);
            return ApiResponse.<String>builder()
            .result(e.toString())
            .build();
        }
    }

    @PostMapping("/{id}/upload-wallpaper")
    public ApiResponse<String> uploadWallpaper(@PathVariable String id, @RequestParam("image") MultipartFile image) {
        try {
            String oldfileName = image.getOriginalFilename();
            int dotIndex = oldfileName.lastIndexOf('.');
            String extension = oldfileName.substring(dotIndex);
            String fileName = String.valueOf(imageCount) + extension;
            imageCount++;
            String imageDirectory = "/root/project/nginx/images";
            Path filePath = Paths.get(imageDirectory, fileName);
            Files.write(filePath, image.getBytes());
            String fileSaved = "http://14.225.254.35/images/" + fileName;
            int savedImage = userService.saveWallpaperImage(id,fileSaved);
            if(savedImage == 1){
                return ApiResponse.<String>builder()
                        .result("Upload image successfully")
                        .build();
            }
            else {
                return ApiResponse.<String>builder()
                        .result("Upload image not successfully")
                        .build();
            }

        } catch (IOException e) {
            return ApiResponse.<String>builder()
            .result(e.toString())
            .build();
        }
    }

    @PutMapping("/{userId}/update-personal")
    ApiResponse<String> updateUserPersonal(@PathVariable String userId, @RequestBody User request) {
        int updated = userService.updateUserPersonal(userId, request);
        if(updated == -1)
        {
            return ApiResponse.<String>builder()
            .result("Fail to update user")
            .build();
        }
        return ApiResponse.<String>builder()
        .result("Update user successfully")
        .build();
    }
}