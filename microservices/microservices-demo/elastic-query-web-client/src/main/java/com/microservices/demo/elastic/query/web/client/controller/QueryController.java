package com.microservices.demo.elastic.query.web.client.controller;

import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.LiveShareResponse;
import com.microservices.demo.elastic.query.web.client.model.LiveValuesResponse;
import com.microservices.demo.elastic.query.web.client.service.ElasticQueryWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@Controller
@CrossOrigin
@RequestScope
@RequiredArgsConstructor
public class QueryController {

    private final ElasticQueryWebClientService elasticQueryWebClientService;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("elasticQueryWebClientRequestModel",
                ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }

    @PostMapping("/query-by-text")
    public String queryByText(@Valid ElasticQueryWebClientRequestModel requestModel,
                              Model model){
        var responseModel = elasticQueryWebClientService.getShareByC(requestModel);
        List<String> shareList = List.of("SASA","SISE","ANGEN","EREGL");
        LiveValuesResponse getLiveValues = elasticQueryWebClientService.getLiveLiveValues(shareList);
        var myList = calcMyShare(getLiveValues);
        model.addAttribute("elasticQueryClientResponseModels", myList);
        model.addAttribute("liveVolume", responseModel.getShareVolume());
        model.addAttribute("searchText", requestModel.getShareData().getC());
        model.addAttribute("elasticQueryClientRequestModel",
                ElasticQueryWebClientRequestModel.builder().build());
        log.info("Returning from reactive client controller for text {} !", requestModel.getShareData().getC());
        return "home";
    }

    private List<LiveShareResponse> calcMyShare(LiveValuesResponse getLiveValues) {
        return getLiveValues.getLiveValues()
                .entrySet()
                .stream()
                .map(entry -> LiveShareResponse.builder()
                        .id(UUID.randomUUID().toString())
                        .description(entry.getKey())
                        .last(entry.getValue().toString())
                        .createdAt(ZonedDateTime.now().toString())
                        .build())
                .toList();

    }


}
