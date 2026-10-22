package com.scheduleappdevelop.user.dto;

import com.scheduleappdevelop.comment.dto.GetCommentResponse;
import com.scheduleappdevelop.schedule.dto.GetScheduleResponse;
import com.scheduleappdevelop.schedule.dto.GetSchedulesResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetUserResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    private final List<GetSchedulesResponse> scheduleList;
    private final List<GetCommentResponse> commentList;
}
