package com.example.together.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.together.model.Relationship;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.together.dto.request.UpdatePasswordRequest;
import com.example.together.dto.request.UserCreationRequest;
import com.example.together.dto.request.UserUpdateRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.UserResponse;
import com.example.together.exception.AppException;
import com.example.together.exception.ErrorCode;
import com.example.together.mapper.UserMapper;
import com.example.together.model.User;
import com.example.together.repository.UserRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    ModelMapper mapper;
    public UserResponse createUser(UserCreationRequest request){
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    public List<UserResponse> getUsers(){
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }


    public UserResponse updatePassword(String id, UpdatePasswordRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if(request.getPassword().equals(request.getConfirmPassword())){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            return userMapper.toUserResponse(userRepository.save(user));
        }else{
            throw new AppException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }

    public int saveAvatarImage(String id, String fileSaved) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAvatar_path(fileSaved);
            userRepository.save(user);
            return 1;
        }
        else {
            return 0;
        }
    }

    public int saveWallpaperImage(String id, String fileSaved) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setWallpaper_path(fileSaved);
            userRepository.save(user);
            return 1;
        }
        else {
            return 0;
        }
    }

    public int updateUserPersonal(String id, UserUpdateRequest user)
    {
        if (!userRepository.existsById(id))
        {
            return -1;
        }
        Optional<User> userOptional = userRepository.findById(id);
        User u = userOptional.get();
        if(user.getUsername() != null) u.setUsername(user.getUsername());
        if(user.getPhone() != null) u.setPhone(user.getPhone());
        if(user.getGender() != null) u.setGender(user.getGender());
        if(user.getEmail() != null) u.setEmail(user.getEmail());
        if(user.getBios() != null) u.setBios(user.getBios());
        if(user.getDob() != null) u.setDob(user.getDob());
        userRepository.save(u);
        return 1;
    }
    public int checkIfCurrentPasswordIsCorrect(String id, String password)
    {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) return -1;
        User user = userOptional.get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if(!passwordEncoder.matches(password, user.getPassword())) return 0;
        return 1;
    }

    public ApiResponse<String> changeUserPassword(String id, String password)
    {
        Optional<User> userOptional = userRepository.findById(id);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ApiResponse.<String>builder()
            .result("Đổi mât khẩu thành công")
            .build();
    }

    public List<UserResponse> getSendFriendRequests(String id)
    {
        List<User> listSendUserFriendRequests = userRepository.getListSendUserFriendRequests(id);
        return listSendUserFriendRequests.stream()
                .map(user -> mapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getSendedFriendRequests(String id) {
        List<User> listSendedUserFriendRequests = userRepository.getListSendedUserFriendRequests(id);
        return listSendedUserFriendRequests.stream()
                .map(user -> mapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getListFriend(String id) {
        List<User> listFriend = userRepository.getListFriends(id);
        return listFriend.stream()
                .map(user -> mapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }


    public List<UserResponse> getBlockUsers(String id) {
        List<User> listBlockUsers = userRepository.getListBlockUsers(id);
        return listBlockUsers.stream()
                .map(user -> mapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    public List<UserResponse> searchPeopleOnKeyBoard(String keyword, Integer keyboard) {
        if(keyboard == 1)
        {
            List<User> listUserSearched = userRepository.searchPeopleOnKeyBoard(keyword);
            return listUserSearched.stream()
                    .map(user -> mapper.map(user, UserResponse.class))
                    .collect(Collectors.toList());
        }
        else
        {
            List<User> listUserSearched = userRepository.searchPeople(keyword);
            return listUserSearched.stream()
                    .map(user -> mapper.map(user, UserResponse.class))
                    .collect(Collectors.toList());
        }
    }
    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse updateUser1(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USER));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getUser1(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USER)));
    }
}