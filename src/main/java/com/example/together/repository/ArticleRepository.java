package com.example.together.repository;

import com.example.together.dto.response.ArticleResponse;
import com.example.together.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String> {
    @Query("SELECT a FROM Article a WHERE a.user_id = :userId ORDER BY a.created_at DESC")
    Page<Article> findAllByUser_id(@Param("userId") String userId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM reaction WHERE reaction.article_id = :articleId AND reaction.liked = 1", nativeQuery = true)
    Integer countLikesByArticleId(@Param("articleId") String articleId);

    @Query("SELECT a FROM Article a WHERE a.user_id = :userId AND a.id = :articleId")
    Optional<Article> findArticleByUserIdAndArticleId(@Param("userId") String userId, @Param("articleId") String articleId);

    @Query("SELECT a FROM Article a WHERE a.id = :articleId")
    Optional<Article> findArticleByArticleId(@Param("articleId") String articleId);

    @Query(value = """
    SELECT new com.example.together.dto.response.ArticleResponse(
        a.id, a.user_id, u.username, u.avatar_path, a.content, a.access, a.created_at)
    FROM User u
    JOIN Article a ON a.user_id = u.id
    WHERE (u.id = :userId
           OR u.id IN (
               SELECT u2.id
               FROM User u2
               JOIN Relationship r ON (r.user1_id = u2.id AND r.user2_id = :userId)
                                 OR (r.user1_id = :userId AND r.user2_id = u2.id)
               WHERE r.status = 'FRIEND'
           ))
      AND a.access = 'FRIEND'
    ORDER BY a.created_at DESC
""")
    Page<ArticleResponse> findArticlesRelativeToUserId(@Param("userId") String userId, Pageable pageable);


}
