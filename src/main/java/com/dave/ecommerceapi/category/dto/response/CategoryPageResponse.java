package com.dave.ecommerceapi.category.dto.response;

import java.util.List;

public record CategoryPageResponse(List<CategoryResponse> content, int page, int size, long totalElements, int totalPages,boolean first, boolean last) {
}
