package com.example.together.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.Multipart;

@Service

public class FileService {
    UserService userService;
    @Value("${spring.image.directory}")
    protected String imageDirectory;

    @Value("${spring.image.url}")
    protected String imageUrl;

    public FileService(UserService userService) {
        this.userService = userService;
    }

    public void HandleUploadingAvatarOrWallpapper(String userId, MultipartFile image, String type) throws IOException
    {
        String oldfileName = image.getOriginalFilename();
        int dotIndex = oldfileName.lastIndexOf('.');
        String extension = oldfileName.substring(dotIndex);
        long timestamp = System.currentTimeMillis();
        String fileName = String.valueOf(timestamp) + extension;
        Path filePath = Paths.get(imageDirectory, fileName);
        Files.write(filePath, image.getBytes());
        String fileSaved = imageUrl + fileName;
        int saveImage;
        if(type.equals("avatar"))
        {
            saveImage = userService.saveAvatarImage(userId, fileSaved);
        }
        else
        {
            saveImage = userService.saveWallpaperImage(userId, fileSaved);
        }
    }
}
