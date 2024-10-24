package com.example.together.repository;

import com.example.together.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("SELECT COUNT(r) FROM Comment r WHERE r.article_id = :article_id")
    Integer countCommentByArticleId(@Param("article_id") String article_id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment r where r.article_id = :article_id")
    void deleteCommentByArticleId(@Param("article_id") String article_id);

    @Query("SELECT r FROM Comment r where r.article_id = :article_id ORDER BY r.created_at ASC")
    List<Comment> findAllByArticleId(@Param("article_id") String article_id);
}
