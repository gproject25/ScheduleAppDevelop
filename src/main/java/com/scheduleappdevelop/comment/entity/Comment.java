package com.scheduleappdevelop.comment.entity;

import com.scheduleappdevelop.config.BaseEntity;
import com.scheduleappdevelop.schedule.entity.Schedule;
import com.scheduleappdevelop.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    //soft delete
    @Column(nullable = false)
    private boolean deleted = false;
    public void softDelete() {
        this.deleted = true;
    }

    public Comment(String content, Schedule schedule, User user){
        this.content = content;
        this.schedule = schedule;
        this.user = user;
    }

    public void update(@NotBlank(message = "내용이 공백일 수 없습니다") String content) {
        this.content = content;
    }
}
