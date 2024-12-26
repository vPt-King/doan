package com.example.together.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String password;
    private String username;
    private String phone;
    private String gender;
    private String bios;
    private String avatar_path;
    private String wallpaper_path;
    private LocalDateTime created_at;
    private Integer is_active;
    private LocalDate dob;
    @OneToOne(mappedBy = "user")
    private OTP otp;

    public void setAvatar_path(String str)
    {
        this.avatar_path = str;
    }

    public void setWallpaper_path(String str)
    {
        this.wallpaper_path = str;
    }


}
