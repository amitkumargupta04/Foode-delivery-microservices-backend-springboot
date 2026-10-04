package com.fooddelivery.admin_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Global unified API response structure wrapper used across all endpoints")
public class StandardResponse<T> {

    @Schema(description = "Indicates whether the network operation was completed successfully", example = "true")
    private boolean success;

    @Schema(description = "Human-readable context message explaining the result or error details", example = "Operation completed successfully")
    private String message;

    @Schema(description = "The corresponding HTTP numeric response status code", example = "201")
    private int status;

    @Schema(description = "The core contextual data payload object returned by the API endpoint")
    private T data;

    //    @Schema(description = "Optional structural object containing pagination or tracking metadata details")
//    private Object metaData;
    private PaginationMetaData metaData;
}
