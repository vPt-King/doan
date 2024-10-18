package com.example.together.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import com.example.together.enumconfig.FileType;
import com.example.together.model.File;
import com.example.together.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.Multipart;

@Service
public class FileService {
    FileRepository fileRepository;
    UserService userService;
    @Value("${spring.file.directory}")
    protected String fileDirectory;

    @Value("${spring.file.url}")
    protected String fileUrl;

    public FileService(UserService userService, FileRepository fileRepository) {
        this.userService = userService;
        this.fileRepository = fileRepository;
    }

    public void HandleUploadingAvatarOrWallpapper(String userId, MultipartFile image, String type) throws IOException
    {
        String oldfileName = image.getOriginalFilename();
        int dotIndex = oldfileName.lastIndexOf('.');
        String extension = oldfileName.substring(dotIndex);
        long timestamp = System.currentTimeMillis();
        String fileName = String.valueOf(timestamp) + extension;
        Path filePath = Paths.get(fileDirectory, fileName);
        Files.write(filePath, image.getBytes());
        String fileSaved = fileUrl + fileName;
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

    public void handleUploadListImages(List<MultipartFile> imageFiles, String articleId) throws IOException {
        for(MultipartFile file : imageFiles)
        {
            String oldfileName = file.getOriginalFilename();
            int dotIndex = oldfileName.lastIndexOf('.');
            String extension = oldfileName.substring(dotIndex);
            long timestamp = System.currentTimeMillis();
            String fileName = String.valueOf(timestamp) + extension;
            Path filePath = Paths.get(fileDirectory, fileName);
            Files.write(filePath, file.getBytes());
            String fileSaved = fileUrl + fileName;
            File f = new File(articleId,"",fileSaved,filePath.toString(), FileType.valueOf("IMAGE"));
            fileRepository.save(f);
        }
    }

    public void handleUploadVideo(MultipartFile videoFile, String id) throws IOException {
        String oldfileName = videoFile.getOriginalFilename();
        int dotIndex = oldfileName.lastIndexOf('.');
        String extension = oldfileName.substring(dotIndex);
        long timestamp = System.currentTimeMillis();
        String fileName = String.valueOf(timestamp) + extension;
        Path filePath = Paths.get(fileDirectory, fileName);
        Files.write(filePath, videoFile.getBytes());
        String fileSaved = fileUrl + fileName;
        File f = new File(id,"",fileSaved,filePath.toString(), FileType.valueOf("VIDEO"));
        fileRepository.save(f);
    }
}
