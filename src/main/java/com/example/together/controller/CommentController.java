package com.example.together.controller;

import com.example.together.dto.CommentDto;
import com.example.together.dto.request.CommentEditRequest;
import com.example.together.dto.request.CommentRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.CommentArticleResponse;
import com.example.together.service.CommentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/comment")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

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

    @PostMapping("/upload-file-comment")
    ApiResponse<String> uploadFileComment(@RequestParam("article_id") String article_id,
                                          @RequestParam("content") String content,
                                          @RequestParam("user_id") String user_id,
                                          @RequestParam(value="parent_comment_id", required = false) String parent_comment_id,
                                          @RequestPart(value = "file", required = false) MultipartFile file)
    {
        try{
            return ApiResponse.<String>builder()
                    .result(commentService.uploadFileComment(article_id, content,user_id, parent_comment_id, file))
                    .build();
        }
        catch (Exception e){
            return ApiResponse.<String>builder()
                    .result(e.getMessage())
                    .build();
        }
    }
}
