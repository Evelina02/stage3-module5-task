package com.mjc.school.service.dto;

import java.util.ArrayList;
import java.util.List;

public record PageDtoResponse<T>(List<T> modelDtoList, int currentPage, int pageCount) {
    public PageDtoResponse {
        if (modelDtoList == null) {
            modelDtoList = new ArrayList<>();
        }
    }
}
