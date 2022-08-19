package com.microservices.demo.kafka.streams.service.controller;

import com.microservices.demo.kafka.streams.service.model.KafkaStreamsResponseModel;
import com.microservices.demo.kafka.streams.service.runner.StreamsRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/", produces = "application/json")
public class KafkaStreamsController {

    private final StreamsRunner<String, Long> streamsRunner;

    @GetMapping("/share-streams/{shareName}")
    @ResponseBody
    public ResponseEntity<KafkaStreamsResponseModel> getShareStreams(@PathVariable @NotEmpty String shareName) {
       var response = streamsRunner.getValueByKey (shareName);
       log.info("Response: {}", response);
       return ResponseEntity.ok(KafkaStreamsResponseModel.builder()
               .shareName(shareName)
               .shareVolume(response)
               .build());
    }


}
