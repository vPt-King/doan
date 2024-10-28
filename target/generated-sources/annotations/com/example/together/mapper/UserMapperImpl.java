package com.example.together.mapper;

import com.example.together.dto.request.UserCreationRequest;
import com.example.together.dto.request.UserUpdateRequest;
import com.example.together.dto.response.UserResponse;
import com.example.together.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.40.0.v20240919-1711, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setDob( request.getDob() );
        user.setEmail( request.getEmail() );
        user.setPassword( request.getPassword() );
        user.setUsername( request.getUsername() );

        return user;
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.avatar_path( user.getAvatar_path() );
        userResponse.bios( user.getBios() );
        userResponse.created_at( user.getCreated_at() );
        userResponse.dob( user.getDob() );
        userResponse.email( user.getEmail() );
        userResponse.gender( user.getGender() );
        userResponse.id( user.getId() );
        userResponse.is_active( user.getIs_active() );
        userResponse.phone( user.getPhone() );
        userResponse.username( user.getUsername() );
        userResponse.wallpaper_path( user.getWallpaper_path() );

        return userResponse.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        user.setBios( request.getBios() );
        user.setDob( request.getDob() );
        user.setEmail( request.getEmail() );
        user.setGender( request.getGender() );
        user.setPhone( request.getPhone() );
        user.setUsername( request.getUsername() );
    }
}
