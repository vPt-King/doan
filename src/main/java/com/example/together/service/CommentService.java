package com.example.together.service;

import com.example.together.dto.CommentDto;
import com.example.together.dto.request.CommentEditRequest;
import com.example.together.dto.request.CommentRequest;
import com.example.together.dto.response.CommentArticleResponse;
import com.example.together.model.Article;
import com.example.together.model.Comment;
import com.example.together.model.User;
import com.example.together.repository.ArticleRepository;
import com.example.together.repository.CommentRepository;
import com.example.together.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileService fileService;

    @Autowired
    private CommentRepository commentRepository;

    public String postComment(CommentRequest request) {
        Optional<Article> articleOptional = articleRepository.findArticleByArticleId(request.getArticle_id());
        if (articleOptional.isPresent()) {
            Comment comment = new Comment();
            comment.setArticle_id(request.getArticle_id());
            comment.setContent(request.getContent());
            comment.setUser_id(request.getUser_id());
            comment.setCreated_at(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
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


    public String deleteComment(CommentEditRequest request) {
        Optional<Article> articleOptional = articleRepository.findArticleByArticleId(request.getArticle_id());
        if (articleOptional.isPresent()) {
            Optional<Comment> commentOptional = commentRepository.findById(request.getComment_id());
            if(commentOptional.isPresent()){
                commentRepository.delete(commentOptional.get());
                return "Xóa bình luận thành công";
            }else{
                return "Comment không tồn tại";
            }
        }
        else{
            return "Bài viết không tồn tại";
        }
    }

    public CommentArticleResponse getCommentArticle(String articleId, int offset, int pageSize) {
        Pageable pageable = PageRequest.of(offset, pageSize);
        List<Comment> comments = commentRepository.findAllByArticleId(articleId, pageable);
        List<CommentDto> commentsDto = new ArrayList<>();
        List<CommentDto> res = new ArrayList<>();
        int[] array = new int[comments.size()];
        int i = 0;
        int total_comment = commentRepository.countCommentByArticleId(articleId);
        for(Comment comment : comments)
        {
            array[i] = 0;
            CommentDto commentDto = new CommentDto();
            User a = userRepository.findById(comment.getUser_id()).get();
            commentDto.setComment_id(comment.getId());
            commentDto.setUser_id(a.getId());
            commentDto.setUsername(a.getUsername());
            commentDto.setAvatar_path(a.getAvatar_path());
            commentDto.setContent(comment.getContent());
            commentDto.setCreated_at(comment.getCreated_at());
            if(comment.getParent_comment_id() != null){
                commentDto.setParent_comment_id(comment.getParent_comment_id());
                array[i] = 1;
            }
            commentsDto.add(commentDto);
            i++;
        }
        i = 0;
        for(CommentDto commentDto : commentsDto)
        {
            if(array[i] == 1){
                CommentDto commentDtoParent = commentsDto.stream().filter(comment -> comment.getComment_id().equals(commentDto.getParent_comment_id())).findFirst().get();
                if(commentDtoParent.getChild_comments() == null){
                    commentDtoParent.setChild_comments(new ArrayList<>());
                }
                commentDtoParent.getChild_comments().add(commentDto);
            }
            i++;
        }
        res = commentsDto.stream().filter(comment -> comment.getParent_comment_id() == null).collect(Collectors.toList());
        return CommentArticleResponse.builder()
                .total_comment(total_comment)
                .res(res)
                .build();
    }

    public String uploadFileComment(String article_id, String content, String user_id, String parent_comment_id, MultipartFile file)
    {
        try {
            Optional<Article> articleOptional = articleRepository.findArticleByArticleId(article_id);
            if (articleOptional.isPresent()) {
                Comment comment = new Comment();
                comment.setArticle_id(article_id);
                comment.setContent(content);
                comment.setUser_id(user_id);
                comment.setCreated_at(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                if (parent_comment_id != null) {
                    comment.setParent_comment_id(parent_comment_id);
                }
                Comment savedComment = commentRepository.save(comment);
                String comment_id = savedComment.getId();
                if (file != null) {
                    fileService.handleUploadFileComment(file, article_id, comment_id);
                }
                return "Tải comment thành công";
            } else {
                return "Bài viết không tồn tại";
            }
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }
}
