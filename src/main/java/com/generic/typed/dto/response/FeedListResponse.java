package com.generic.typed.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FeedListResponse {
    private List<FeedItemResponse> items;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
}
