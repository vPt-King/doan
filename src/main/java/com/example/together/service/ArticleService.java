package com.example.together.service;

import com.example.together.dto.response.ArticleResponse;
import com.example.together.enumconfig.AccessStatus;
import com.example.together.model.Article;
import com.example.together.model.User;
import com.example.together.repository.ArticleRepository;
import com.example.together.repository.FileRepository;
import com.example.together.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleService {
    ArticleRepository articleRepository;
    UserRepository userRepository;
    FileRepository fileRepository;
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
            res.add(new ArticleResponse(article.getId(),u.getId(),u.getUsername(),u.getAvatar_path(),article.getContent(),image_article,video_article,reaction_number,article.getAccess()));
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
}
