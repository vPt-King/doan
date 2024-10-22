package com.example.together.repository;

import com.example.together.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {


    @Query("SELECT a FROM Reaction a WHERE a.article_id = :articleId and a.user_id = :userId")
    Optional<Reaction> findReactionByArticleIdAndUserId(@Param("articleId") String articleId, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Reaction r WHERE r.article_id = :articleId")
    void deleteReactionByArticleId(@Param("articleId") String articleId);
}
