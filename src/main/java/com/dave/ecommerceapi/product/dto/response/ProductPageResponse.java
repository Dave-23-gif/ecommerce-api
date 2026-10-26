package com.dave.ecommerceapi.product.dto.response;

import java.util.List;

public record ProductPageResponse(List<ProductResponse> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
}
