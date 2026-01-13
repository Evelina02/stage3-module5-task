package com.mjc.school.service.dto;

import java.time.LocalDateTime;

public record CommentsDtoResponse(
        Long id,
        String content,
        Long newsId,
        LocalDateTime createdDate,
        LocalDateTime lastUpdatedDate
) {
}

