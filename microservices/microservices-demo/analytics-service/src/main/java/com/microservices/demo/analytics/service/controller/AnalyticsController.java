package com.microservices.demo.analytics.service.controller;

import com.microservices.demo.analytics.service.model.AnalyticsResponseModel;
import com.microservices.demo.analytics.service.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(produces = "application/json")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/get-share-history/{shareName}")
    @Operation(summary = "Get share history by shareName.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnalyticsResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @ResponseBody
    public ResponseEntity<List<AnalyticsResponseModel>> getShareLiveData(@PathVariable @NotEmpty String shareName) {
        return ResponseEntity.ok(analyticsService.getShareNameAnalytics(shareName));
    }

    @GetMapping("get-share-live/{shareName}")
    @Operation(summary = "Get share live data by shareName.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnalyticsResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @ResponseBody
    public ResponseEntity<AnalyticsResponseModel> getAll(@PathVariable @NotEmpty String shareName) {
        return ResponseEntity.ok(analyticsService.getShareLiveDataOnCache(shareName));
    }

    @GetMapping("get-cached-data")
    @Operation(summary = "Get all cached data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnalyticsResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @ResponseBody
    public ResponseEntity<List<AnalyticsResponseModel>> getAll() {
        return ResponseEntity.ok(analyticsService.getAllCacheData());
    }

}
