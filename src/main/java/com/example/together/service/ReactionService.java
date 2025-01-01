package com.example.together.service;

import com.example.together.dto.request.LikeArticleRequest;
import com.example.together.model.Article;
import com.example.together.model.Reaction;
import com.example.together.repository.ArticleRepository;
import com.example.together.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {


    private final ReactionRepository reactionRepository;
    private final ArticleRepository articleRepository;

    public String likeArticle(LikeArticleRequest request) {
        Optional<Article> articleOptional = articleRepository.findArticleByArticleId(request.getArticle_id());
        if(articleOptional.isPresent()) {
            Optional<Reaction> reactionOptional = reactionRepository.findReactionByArticleIdAndUserId(request.getArticle_id(),request.getUser_id());
            if(reactionOptional.isPresent()) {
                Reaction reaction = reactionOptional.get();
                reaction.setLiked(request.getLiked());
                reactionRepository.save(reaction);
            }
            else{
                Reaction reaction = new Reaction();
                reaction.setArticle_id(request.getArticle_id());
                reaction.setUser_id(request.getUser_id());
                reaction.setLiked(request.getLiked());
                reactionRepository.save(reaction);
            }
            return "Cập nhật trạng thái thành công";
        }
        else{
            return "Bài viết không tồn tại";
        }
    }
}
