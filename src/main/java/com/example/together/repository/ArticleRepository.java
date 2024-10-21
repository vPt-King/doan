package com.example.together.repository;

import com.example.together.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String> {
    @Query("SELECT a FROM Article a WHERE a.user_id = :userId")
    Page<Article> findAllByUser_id(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.article_id = :articleId")
    Integer countByArticleId(@Param("articleId") String id);

    @Query("SELECT a FROM Article a WHERE a.user_id = :userId AND a.id = :articleId")
    Optional<Article> findArticleByUserIdAndArticleId(@Param("userId") String userId, @Param("articleId") String articleId);

    @Query("SELECT a FROM Article a WHERE a.id = :articleId")
    Optional<Article> findArticleByArticleId(@Param("articleId") String articleId);
}
