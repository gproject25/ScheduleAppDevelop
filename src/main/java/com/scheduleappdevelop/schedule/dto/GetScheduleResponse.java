package com.scheduleappdevelop.schedule.dto;

import com.scheduleappdevelop.comment.dto.GetCommentResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetScheduleResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String user;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final List<GetCommentResponse> commentList;
}
