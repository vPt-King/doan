package com.example.together.service;

import com.example.together.dto.request.CommentEditRequest;
import com.example.together.dto.request.CommentRequest;
import com.example.together.model.Article;
import com.example.together.model.Comment;
import com.example.together.repository.ArticleRepository;
import com.example.together.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    public String postComment(CommentRequest request) {
        Optional<Article> articleOptional = articleRepository.findArticleByArticleId(request.getArticle_id());
        if (articleOptional.isPresent()) {
            Comment comment = new Comment();
            comment.setArticle_id(request.getArticle_id());
            comment.setContent(request.getContent());
            comment.setUser_id(request.getUser_id());
            comment.setCreated_at(LocalDateTime.now());
            if(request.getParent_comment_id() != null){
                comment.setParent_comment_id(request.getParent_comment_id());
            }
            commentRepository.save(comment);
            return "Tải comment thành công";
        }
        else{
            return "Bài viết không tồn tại";
        }
    }

    public String editComment(CommentEditRequest request) {
        Optional<Article> articleOptional = articleRepository.findArticleByArticleId(request.getArticle_id());
        if (articleOptional.isPresent()) {
            Optional<Comment> commentOptional = commentRepository.findById(request.getComment_id());
            if(commentOptional.isPresent()){
                Comment comment = commentOptional.get();
                comment.setContent(request.getContent());
                commentRepository.save(comment);
                return "Sửa comment thành công";
            }
            else{
                return "Không tồn tại comment";
            }
        }
        else{
            return "Bài viết không tồn tại";
        }
    }
}
