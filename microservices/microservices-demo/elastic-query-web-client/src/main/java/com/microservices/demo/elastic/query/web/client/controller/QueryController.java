package com.microservices.demo.elastic.query.web.client.controller;

import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
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
                              Model model) {
        var responseModel = elasticQueryWebClientService.getShareByC(requestModel);

        model.addAttribute("elasticQueryClientResponseModels", responseModel.getQueryResponseModels());
        model.addAttribute("liveVolume", responseModel.getShareVolume());
        model.addAttribute("searchText", requestModel.getShareData().getC());
        model.addAttribute("elasticQueryClientRequestModel",
                ElasticQueryWebClientRequestModel.builder().build());
        log.info("Returning from reactive client controller for text {} !", requestModel.getShareData().getC());
        return "home";
    }


}
