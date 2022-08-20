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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/", produces = "application/json")
public class AnalyticsController {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsController.class);

    private final AnalyticsService analyticsService;



    @GetMapping("/get-share-live/{shareName}")
    @Operation(summary = "Get analytics by word.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnalyticsResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @ResponseBody
    public ResponseEntity<AnalyticsResponseModel> getShareLiveData(@PathVariable @NotEmpty String shareName) {
        Optional<AnalyticsResponseModel> response = analyticsService.getShareNameAnalytics(shareName);
        if (response.isPresent()) {
            LOG.info("Analytics data returned with id {}", response.get().getId());
            return ResponseEntity.ok(response.get());
        }
        return ResponseEntity.ok(AnalyticsResponseModel.builder().build());
    }
}
