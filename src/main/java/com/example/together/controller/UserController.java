package com.example.together.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.example.together.dto.request.ChangeUserPasswordRequest;
import com.example.together.dto.request.UpdatePasswordRequest;
import com.example.together.dto.request.UserCreationRequest;
import com.example.together.dto.request.UserUpdateRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.UserResponse;
import com.example.together.model.User;
import com.example.together.service.FileService;
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
    FileService fileService;
    
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

    
    @PostMapping("/{id}/upload-image")
    public ApiResponse<String> uploadImage(@PathVariable String id, @RequestParam("image") MultipartFile image,
                                                                     @RequestParam("type") String type) 
    {
        try{
            fileService.HandleUploadingAvatarOrWallpapper(id, image, type);
            return ApiResponse.<String>builder()
                        .result("Tải ảnh lên thành công")
                        .build();
        } catch (IOException e) {
            System.out.println(e);
            return ApiResponse.<String>builder()
            .result("Tải ảnh lên thất bại")
            .build();
        }
    }


    @PutMapping("/{userId}/update-personal")
    ApiResponse<String> updateUserPersonal(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        int updated = userService.updateUserPersonal(userId, request);
        if(updated == -1)
        {
            return ApiResponse.<String>builder()
            .result("Cập nhật thông tin không thành công")
            .build();
        }
        return ApiResponse.<String>builder()
        .result("Cập nhật thông tin thành công")
        .build();
    }

    @PutMapping("/{userId}/change-password")
    ApiResponse<String> changeUserPassword(@PathVariable String userId, @RequestBody ChangeUserPasswordRequest request) {
        int checkCurrentPassword = userService.checkIfCurrentPasswordIsCorrect(userId,request.getCurrentPassword());
        if(checkCurrentPassword == -1)
        {
            return ApiResponse.<String>builder()
                .result("Người dùng không tồn tại")
                .build();
        }
        if(checkCurrentPassword == 0)
        {
            return ApiResponse.<String>builder()
                .result("Mật khẩu hiện tại không chính xác")
                .build();
        }
        return userService.changeUserPassword(userId, request.getNewPassword());
    }

    @GetMapping("/{id}/send-friend") // danh sách đã gửi kết bạn
    ApiResponse<List<UserResponse>> getSendFriendRequest(@PathVariable String id){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getSendFriendRequests(id))
                .build();
    }

    @GetMapping("{id}/sended-friend")
    ApiResponse<List<UserResponse>> getSendedFriendRequest(@PathVariable String id){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getSendedFriendRequests(id))
                .build();
    }

    @GetMapping("{id}/block")
    ApiResponse<List<UserResponse>> getBlockUser(@PathVariable String id){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getBlockUsers(id))
                .build();
    }
}