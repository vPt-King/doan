package com.example.together.mapper;

import com.example.together.dto.request.UserCreationRequest;
import com.example.together.dto.request.UserUpdateRequest;
import com.example.together.dto.response.UserResponse;
import com.example.together.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( request.getEmail() );
        user.setPassword( request.getPassword() );
        user.setUsername( request.getUsername() );
        user.setDob( request.getDob() );

        return user;
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.email( user.getEmail() );
        userResponse.username( user.getUsername() );
        userResponse.phone( user.getPhone() );
        userResponse.gender( user.getGender() );
        userResponse.bios( user.getBios() );
        userResponse.avatar_path( user.getAvatar_path() );
        userResponse.wallpaper_path( user.getWallpaper_path() );
        userResponse.created_at( user.getCreated_at() );
        userResponse.is_active( user.getIs_active() );
        userResponse.dob( user.getDob() );

        return userResponse.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        user.setEmail( request.getEmail() );
        user.setUsername( request.getUsername() );
        user.setPhone( request.getPhone() );
        user.setGender( request.getGender() );
        user.setBios( request.getBios() );
        user.setDob( request.getDob() );
    }
}
