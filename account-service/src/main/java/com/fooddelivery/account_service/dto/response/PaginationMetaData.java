package com.fooddelivery.account_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Pagination metadata")
public class PaginationMetaData {

    @Schema(description = "Current page number", example = "0")
    private int currentPage;

    @Schema(description = "Number of records per page", example = "10")
    private int pageSize;

    @Schema(description = "Total number of records", example = "126")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "13")
    private int totalPages;

    @Schema(description = "Indicates whether next page exists", example = "true")
    private boolean hasNext;

    @Schema(description = "Indicates whether previous page exists", example = "false")
    private boolean hasPrevious;

    @Schema(description = "Indicates whether current page is the first page", example = "true")
    private boolean first;

    @Schema(description = "Indicates whether current page is the last page", example = "false")
    private boolean last;
}
