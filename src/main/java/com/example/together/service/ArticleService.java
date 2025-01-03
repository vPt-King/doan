package com.example.together.service;

import com.example.together.dto.CommentDto;
import com.example.together.dto.request.ArticleDetailRequest;
import com.example.together.dto.request.ArticleRequest;
import com.example.together.dto.response.ArticleResponse;
import com.example.together.enumconfig.AccessStatus;
import com.example.together.model.Article;
import com.example.together.model.Comment;
import com.example.together.model.Reaction;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<ArticleResponse> getArticlesOfUser(String id, int offset, int pageSize, String user_id) {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Article> articles = articleRepository.findAllByUser_id(id,pageable);
        List<ArticleResponse> res = new ArrayList<>();
        User u =  userRepository.findById(id).get();

        for(Article article : articles)
        {
            System.out.println(article.getCreated_at());
            List<String> image_article = fileRepository.findUrlByArticle_id(article.getId());
            String video_article = fileRepository.findVideo_articleByArticle_id(article.getId());
            Integer reaction_number = articleRepository.countLikesByArticleId(article.getId());
            Integer number_comment = commentRepository.countCommentByArticleId(article.getId());
            Optional<Reaction> reactionOptional = reactionRepository.findReactionByArticleIdAndUserId(article.getId(), user_id);
            int reaction = 0;
            if(reactionOptional.isPresent())
            {
                reaction = 1;
            }
            res.add(new ArticleResponse(article.getId(),u.getId(),u.getUsername(),u.getAvatar_path(),article.getContent(),image_article,video_article,reaction_number,article.getAccess(),article.getCreated_at(),number_comment,reaction));
        }
        return res;
    }

    public Article handleUploadArticle(String id, String content, AccessStatus accessStatus) {
        Article article = new Article();
        article.setUser_id(id);
        article.setContent(content);
        article.setAccess(accessStatus);
        article.setCreated_at(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        article.setCaption("Đã đăng bài viết");
        System.out.println(article.getCreated_at());
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

    public Page<ArticleResponse> getNews(String userId, int offset, int pageSize)
    {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<ArticleResponse> articles = articleRepository.findArticlesRelativeToUserId(userId,pageable);
        for(ArticleResponse article : articles)
        {
            List<String> image_article = fileRepository.findUrlByArticle_id(article.getId());
            String video_article = fileRepository.findVideo_articleByArticle_id(article.getId());
            Integer reaction_number = articleRepository.countLikesByArticleId(article.getId());
            Integer number_comment = commentRepository.countCommentByArticleId(article.getId());
            article.setImage_article(image_article);
            article.setVideo_article(video_article);
            article.setNumber_reaction(reaction_number);
            article.setNumber_comment(number_comment);
            Optional<Reaction> reactionOptional = reactionRepository.findReactionByArticleIdAndUserId(article.getId(), userId);
            if(reactionOptional.isPresent())
            {
                article.setReaction(1);
            }
            else article.setReaction(0);
        }
        return articles;
    }

    public ArticleResponse getDetailArticle(String userId, ArticleDetailRequest request) {
        ArticleResponse articleResponse = new ArticleResponse();
        Optional<Article> articleOptional = articleRepository.findArticleByArticleId(request.getArticle_id());
        if(articleOptional.isPresent()) {
            Article article = articleOptional.get();
            articleResponse.setMessage("Tìm thấy bài viết");
            User u = userRepository.findById(request.getOwner_id()).get();
            List<String> image_article = fileRepository.findUrlByArticle_id(article.getId());
            String video_article = fileRepository.findVideo_articleByArticle_id(article.getId());
            Integer reaction_number = articleRepository.countLikesByArticleId(article.getId());
            articleResponse.setId(article.getId());
            articleResponse.setUser_id(u.getId());
            articleResponse.setUsername(u.getUsername());
            articleResponse.setUser_avatar(u.getAvatar_path());
            articleResponse.setContent(article.getContent());
            articleResponse.setImage_article(image_article);
            articleResponse.setVideo_article(video_article);
            articleResponse.setNumber_reaction(reaction_number);
            articleResponse.setAccess_status(article.getAccess());
            articleResponse.setTime(article.getCreated_at());
            Optional<Reaction> reactionOptional = reactionRepository.findReactionByArticleIdAndUserId(article.getId(), userId);
            int reaction = 0;
            if(reactionOptional.isPresent())
            {
                reaction = 1;
            }
            articleResponse.setReaction(reaction);
        }
        else{
            articleResponse.setMessage("Không tìm thấy bài viết");
        }
        return articleResponse;
    }
}
