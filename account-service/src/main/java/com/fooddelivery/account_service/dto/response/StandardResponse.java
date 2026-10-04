package com.fooddelivery.account_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API Response Wrapper")
public class StandardResponse<T> {

    @Schema(description = "Indicates whether the request was successful", example = "true")
    private boolean success;

    @Schema(description = "Response summary message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "HTTP Status Code", example = "200")
    private int statusCode;

    @Schema(description = "Payload data returned by the API endpoint")
    private T data;

    @Schema(description = "Meta Data..", example = "Will implement later..")
    private Object metaData;
   // private PaginationMetaData metaData;

}
