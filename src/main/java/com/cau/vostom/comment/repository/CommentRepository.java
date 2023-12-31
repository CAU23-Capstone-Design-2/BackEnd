package com.cau.vostom.comment.repository;

import com.cau.vostom.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteById(Long commentId);

    List<Comment> findAllByUserId(Long userId);

    List<Comment> findAllByMusicId(Long musicId);
}
