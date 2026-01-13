package com.mjc.school.service.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;

import com.mjc.school.service.validator.constraint.Size;

public record UpdateNewsDtoRequest(
    @Nullable
    @Size(min = 5, max = 30)
    String title,

    @Nullable
    @Size(min = 5, max = 255)
    String content,

    @Nullable
    String author,

    @Nullable
    List<String> tags,

    @Nullable
    List<Long> commentsIds
) {
     public UpdateNewsDtoRequest {
         if (tags == null) {
             tags = new ArrayList<>();
         }
         if (commentsIds == null) {
            commentsIds = new ArrayList<>();
         }
     }
}
