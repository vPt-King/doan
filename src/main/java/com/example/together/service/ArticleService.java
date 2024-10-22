package com.example.together.service;

import com.example.together.dto.request.ArticleRequest;
import com.example.together.dto.response.ArticleResponse;
import com.example.together.enumconfig.AccessStatus;
import com.example.together.model.Article;
import com.example.together.model.User;
import com.example.together.repository.*;
import jakarta.persistence.Access;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleService {
    ArticleRepository articleRepository;
    UserRepository userRepository;
    FileRepository fileRepository;
    CommentRepository commentRepository;
    FileService fileService;
    private final ReactionRepository reactionRepository;

    public List<ArticleResponse> getArticlesOfUser(String id, int offset, int pageSize) {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Article> articles = articleRepository.findAllByUser_id(id,pageable);
        List<ArticleResponse> res = new ArrayList<>();
        User u =  userRepository.findById(id).get();

        for(Article article : articles)
        {
            List<String> image_article = fileRepository.findUrlByArticle_id(article.getId());
            String video_article = fileRepository.findVideo_articleByArticle_id(article.getId());
            Integer reaction_number = articleRepository.countByArticleId(article.getId());
            Integer number_comment = commentRepository.countCommentByArticleId(article.getId());
            res.add(new ArticleResponse(article.getId(),u.getId(),u.getUsername(),u.getAvatar_path(),article.getContent(),image_article,video_article,reaction_number,article.getAccess(),article.getCreated_at(),number_comment));
        }
        return res;
    }

    public Article handleUploadArticle(String id, String content, AccessStatus accessStatus) {
        Article article = new Article();
        article.setUser_id(id);
        article.setContent(content);
        article.setAccess(accessStatus);
        article.setCreated_at(LocalDateTime.now());
        article.setCaption("Đã đăng bài viết");
        return articleRepository.save(article);
    }

    public String handleEditArticle(String userId, String articleId, String content, AccessStatus accessStatus, List<MultipartFile> imageFiles, MultipartFile videoFile) {
        Optional<Article> articleOptional = articleRepository.findArticleByUserIdAndArticleId(userId,articleId);
        if(articleOptional.isPresent()) {
            Article article = articleOptional.get();
            if (content != null) {
                article.setContent(content);
            }
            if (accessStatus != null) {
                article.setAccess(accessStatus);
            }
            try{
                if (imageFiles != null)
                {
                    fileRepository.deleteImagesByArticleId(articleId);
                    fileService.handleUploadListImages(imageFiles,articleId);
                }
                if(videoFile != null)
                {
                    fileRepository.deleteVideoByArticleId(articleId);
                    fileService.handleUploadVideo(videoFile,articleId);
                }
                articleRepository.save(article);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            return "Cập nhật bài viết thành công";
        }
        else{
            return "Bài viết không tồn tại";
        }
    }

    public String deleteArticle(ArticleRequest request) {
        Optional<Article> articleOptional = articleRepository.findArticleByArticleId(request.getArticle_id());
        if(articleOptional.isPresent()) {
            fileRepository.deleteImagesByArticleId(request.getArticle_id());
            fileRepository.deleteVideoByArticleId(request.getArticle_id());
            reactionRepository.deleteReactionByArticleId(request.getArticle_id());
            commentRepository.deleteCommentByArticleId(request.getArticle_id());
            articleRepository.delete(articleOptional.get());
            return "Xóa bài viết thành công";
        }
        else{
            return "Không tồn tại bài viết";
        }
    }
}
