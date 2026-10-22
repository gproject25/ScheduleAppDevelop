package com.scheduleappdevelop.comment.repository;

import com.scheduleappdevelop.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndDeletedFalse(Long commentId);

    @Query("SELECT c FROM Comment c JOIN c.user u WHERE c.deleted = false AND (:userId IS NULL OR u.id = :userId)")
    List<Comment> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Comment c JOIN c.schedule s WHERE s.deleted = false AND s.id = :scheduleId")
    List<Comment> findAllByScheduleId(@Param("scheduleId")Long scheduleId);
}
