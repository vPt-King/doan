package com.example.together.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    private String id;
    private String email;
    private String username;
    private String phone;
    private String gender;
    private String bios;
    private String avatar_path;
    private String wallpaper_path;
    private LocalDateTime created_at;
    private Integer is_active;
    private LocalDate dob;
}
