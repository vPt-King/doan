package com.example.together.repository;

import com.example.together.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {


    @Query("SELECT a FROM Reaction a WHERE a.article_id = :articleId and a.user_id = :userId")
    Optional<Reaction> findReactionByArticleIdAndUserId(@Param("articleId") String articleId, @Param("userId") String userId);
}
