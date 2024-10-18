package com.example.together.service;

import com.example.together.enumconfig.AccessStatus;
import com.example.together.model.Article;
import com.example.together.repository.ArticleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleService {
    ArticleRepository articleRepository;
    public Article handleUploadArticle(String id, String content, AccessStatus accessStatus) {
        Article article = new Article();
        article.setUser_id(id);
        article.setContent(content);
        article.setAccess(accessStatus);
        article.setCreated_at(LocalDateTime.now());
        return articleRepository.save(article);
    }
}
