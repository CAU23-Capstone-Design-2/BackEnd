package com.cau.vostom.user.repository;

import com.cau.vostom.user.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteById(Long commentId);
}
