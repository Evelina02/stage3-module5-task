package com.mjc.school.service.filter.mapper;

import org.springframework.stereotype.Component;

import com.mjc.school.service.dto.ResourceSearchFilterRequestDTO;
import com.mjc.school.service.filter.ResourceSearchFilter;

@Component
public class NewsSearchFilterMapper extends BaseSearchFilterMapper<ResourceSearchFilterRequestDTO> {

    private final static String TITLE_FIELD_TO_SORT_BY = "title";

    private final static String TITLE_FIELD_SORT_ORDER = "ASC";

    private final static String CREATED_DATE_FIELD_TO_SORT_BY = "createdDate";

    private final static String CREATED_DATE_FIELD_SORT_ORDER = "ASC";

    @Override
    public ResourceSearchFilter map(ResourceSearchFilterRequestDTO searchFilterRequest) {
        DEFAULT_SORTING_MAP.put(TITLE_FIELD_TO_SORT_BY, TITLE_FIELD_SORT_ORDER);
        DEFAULT_SORTING_MAP.put(CREATED_DATE_FIELD_TO_SORT_BY, CREATED_DATE_FIELD_SORT_ORDER);
        return createResourceSearchFilter(searchFilterRequest.getPage(), searchFilterRequest.getPageSize(), searchFilterRequest.getSortByAndOrder(), searchFilterRequest.getSearchFilter());
    }

}
