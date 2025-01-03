package com.example.together.controller;

import com.example.together.dto.CommentDto;
import com.example.together.dto.request.CommentEditRequest;
import com.example.together.dto.request.CommentRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.CommentArticleResponse;
import com.example.together.model.Comment;
import com.example.together.service.CommentService;
import com.example.together.service.FileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/comment")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;
    FileService fileService;

    @GetMapping("/get-comments-article/{articleId}/{offset}/{pageSize}")
    ApiResponse<CommentArticleResponse> getCommentsArticle(@PathVariable String articleId, @PathVariable int offset, @PathVariable int pageSize)
    {
        return ApiResponse.<CommentArticleResponse>builder()
                .result(commentService.getCommentArticle(articleId, offset, pageSize))
                .build();
    }


    @PostMapping("/post-comment")
    ApiResponse<String> postComment(@RequestBody CommentRequest request)
    {
        return ApiResponse.<String>builder()
                .result(commentService.postComment(request))
                .build();
    }

    @PostMapping("/post-comment-update")
    ApiResponse<String> postCommentUpdate(
            @RequestPart("comment_request") CommentRequest request,
            @RequestPart(value = "image_files", required = false) List<MultipartFile> imageFiles
    ) {
        try {
            // Gọi service để lưu thông tin bình luận
            String comment = commentService.postComment(request);

            // Nếu có danh sách ảnh được gửi kèm, xử lý lưu ảnh liên quan đến bình luận
            if (imageFiles != null && !imageFiles.isEmpty()) {
                fileService.handleUploadListImages(imageFiles, comment);
            }

            // Trả về kết quả thành công
            return ApiResponse.<String>builder()
                    .result("Bình luận thành công")
                    .build();
        } catch (IllegalArgumentException | IOException e) {
            // Xử lý lỗi và trả về thông báo lỗi
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder()
                    .result(e.getMessage())
                    .build();
        }
    }


    @PutMapping("/edit-comment")
    ApiResponse<String> editComment(@RequestBody CommentEditRequest request)
    {
        return ApiResponse.<String>builder()
                .result(commentService.editComment(request))
                .build();
    }

    @DeleteMapping("/delete-comment")
    ApiResponse<String> deleteComment(@RequestBody CommentEditRequest request)
    {
        return ApiResponse.<String>builder()
                .result(commentService.deleteComment(request))
                .build();
    }
}
