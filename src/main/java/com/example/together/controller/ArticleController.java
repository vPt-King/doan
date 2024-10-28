package com.example.together.controller;

import com.example.together.dto.request.ArticleDetailRequest;
import com.example.together.dto.request.ArticleRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.ArticleResponse;
import com.example.together.enumconfig.AccessStatus;
import com.example.together.model.Article;
import com.example.together.service.ArticleService;
import com.example.together.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

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

    @GetMapping("/{id}/get-articles/{offset}/{pageSize}")
    ApiResponse<List<ArticleResponse>> getArticlesOfUser(@PathVariable String id, @PathVariable int offset, @PathVariable int pageSize) {
        return ApiResponse.<List<ArticleResponse>>builder()
                .result(articleService.getArticlesOfUser(id,offset,pageSize))
                .build();
    }

    @PutMapping("/{user_id}/edit-article/{article_id}")
    ApiResponse<String> editArticle(@PathVariable String user_id,
                                    @PathVariable String article_id,
                                    @RequestParam("content") String content,
                                    @RequestParam("access_status") String accessStatus,
                                    @RequestPart(value = "image_files", required = false) List<MultipartFile> imageFiles,
                                    @RequestPart(value = "video_file", required = false) MultipartFile videoFile) {
        return ApiResponse.<String>builder()
                .result(articleService.handleEditArticle(user_id,article_id,content,AccessStatus.valueOf(accessStatus),imageFiles,videoFile))
                .build();
    }

    @DeleteMapping("/delete-article")
    ApiResponse<String> deleteArticle(@RequestBody ArticleRequest request) {
        return ApiResponse.<String>builder()
                .result(articleService.deleteArticle(request))
                .build();
    }

    @GetMapping("{userId}/get-news/{offset}/{pageSize}")
    ApiResponse<Page<ArticleResponse>> getNews(@PathVariable String userId, @PathVariable int offset, @PathVariable int pageSize) {
        return ApiResponse.<Page<ArticleResponse>>builder()
                .result(articleService.getNews(userId,offset,pageSize))
                .build();
    }

    @PostMapping("/detail-article")
    ApiResponse<ArticleResponse> getArticleDetail(@RequestBody ArticleDetailRequest request) {
        return ApiResponse.<ArticleResponse>builder()
                .result(articleService.getDetailArticle(request))
                .build();
    }
}
