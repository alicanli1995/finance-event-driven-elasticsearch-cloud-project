package com.finance.stream.api.scheduler;

import com.finance.stream.api.client.FinanceService;
import com.finance.stream.api.service.ProcessData;
import com.microservices.demo.config.FinanceDataStreamConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceStreamScheduler {
    private final FinanceService financeService;
    private final FinanceDataStreamConfig financeDataStreamConfig;
    private final ProcessData processData;

//    @Scheduled( cron = "0/20 * 10-18  * * MON-FRI",
//                zone = "Europe/Istanbul")
    @Scheduled(fixedDelay = 20000)
    public void getBistInformation() {
        financeDataStreamConfig
                .getShareList()
                .forEach(bist ->
                {
                    var endeksUrl = bist.concat(financeDataStreamConfig.getUrlAppend());
                    var bistShare = financeService.getBISTInformation(endeksUrl);

                    if (bistShare.isEmpty()){
                        log.error("Error on getting information... Try later !");
                        throw new RuntimeException("Error on getting information... Try later !");
                    }

                    try {
                        processData.processData(bistShare);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        log.error("Error in interrupted or processing data with kafka: {}",  e.getMessage());
                    }
                });

    }

}

