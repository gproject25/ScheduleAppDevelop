package com.scheduleappdevelop.comment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GetCommentResponse {
    private final Long id;
    private final String content;
    private final String author;
    private final LocalDateTime createAt;
    private final LocalDateTime modifiedAt;
}
