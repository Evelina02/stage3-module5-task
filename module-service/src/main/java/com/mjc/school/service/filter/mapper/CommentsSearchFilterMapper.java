package com.mjc.school.service.filter.mapper;

import org.springframework.stereotype.Component;

import com.mjc.school.service.dto.ResourceSearchFilterRequestDTO;
import com.mjc.school.service.filter.ResourceSearchFilter;

@Component
public class CommentsSearchFilterMapper extends BaseSearchFilterMapper<ResourceSearchFilterRequestDTO> {
    private final static String NAME_FIELD_TO_SORT_BY = "createdDate";

    private final static String NAME_FIELD_SORT_ORDER = "ASC";

    @Override
    public ResourceSearchFilter map(ResourceSearchFilterRequestDTO searchFilterRequest) {
        DEFAULT_SORTING_MAP.put(NAME_FIELD_TO_SORT_BY, NAME_FIELD_SORT_ORDER);
        return createResourceSearchFilter(searchFilterRequest.getPage(), searchFilterRequest.getPageSize(), searchFilterRequest.getSortByAndOrder(), searchFilterRequest.getSearchFilter());
    }
}
