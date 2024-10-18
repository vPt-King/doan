package com.example.together.controller;

import com.example.together.dto.response.ApiResponse;
import com.example.together.enumconfig.AccessStatus;
import com.example.together.model.Article;
import com.example.together.service.ArticleService;
import com.example.together.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleController {
    FileService fileService;
    ModelMapper modelMapper;
    ArticleService articleService;
    @PostMapping("/{id}/post-article")
    ApiResponse<String> postArticle(
            @PathVariable String id,
            @RequestParam("content") String content,
            @RequestParam("access_status") String accessStatus,
            @RequestPart(value = "image_files", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "video_file", required = false) MultipartFile videoFile) {
        try{
            Article artilce = articleService.handleUploadArticle(id, content, AccessStatus.valueOf(accessStatus));
            if(imageFiles != null) {
                fileService.handleUploadListImages(imageFiles,artilce.getId());
            }
            if(videoFile != null) {
                fileService.handleUploadVideo(videoFile,artilce.getId());
            }

            return ApiResponse.<String>builder()
                    .result("Đăng thành công")
                    .build();
        }catch (IllegalArgumentException | IOException e)
        {
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder()
                    .result(e.getMessage())
                    .build();
        }

    }
}
